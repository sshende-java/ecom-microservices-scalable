package com.ecommerce.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/products/**").hasRole("PRODUCT")
                        .pathMatchers("/api/orders/**").hasRole("ORDER")
                        .pathMatchers("/api/users/**").hasRole("USER")
                        .anyExchange().authenticated())  //all request should be authenticated
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                                jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor())
                        )
                )
                .build();
    }


    //Extract Roles from JWT Token
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsMap("resource_access").entrySet().stream()
                    .filter(entry -> entry.getKey().equals("oauth2-pkce"))
                    .flatMap(entry -> ((Map<String, List<String>>) entry.getValue()).get("roles").stream())
                    .toList();

            System.out.println("Extracted Roles: " + roles);

            return Flux.fromIterable(roles).map(role -> new SimpleGrantedAuthority("ROLE_" + role));
        });

        return jwtAuthenticationConverter;
    }

    /* Way 2 --
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        return new ReactiveJwtAuthenticationConverterAdapter(jwt -> {
            Collection<SimpleGrantedAuthority> authorities = extractRoles(jwt);
            return Mono.just(new JwtAuthenticationToken(jwt, authorities)).block();
        });
    }

    private Collection<SimpleGrantedAuthority> extractRoles(Jwt jwt) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null && resourceAccess.containsKey("oauth2-pkce")) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("oauth2-pkce");
            List<String> roles = (List<String>) clientAccess.get("roles");

            if (roles != null) {
                authorities.addAll(roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList()));
            }
        }

        return authorities;
    }
    -- */

}
