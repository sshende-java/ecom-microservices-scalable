package com.ecommerce.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

//Java based Routing config instead of yml , beneficial for adding filters or modifying request itself
@Configuration
public class GatewayConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        //explanation below table
        return new RedisRateLimiter(10, 20, 1);
    }

    //KeyResolver is an interface used by Spring Cloud Gateway to resolve a key for rate limiting.
    //rate limiting based on user hostname
    @Bean
    public KeyResolver hostNameKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest().getRemoteAddress().getHostName()
        );
    }


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-microservice", r -> r.path("/api/products/**")
                        .filters(f -> f.circuitBreaker(config -> config.setName("ecomCircuitBreaker")
                                .setFallbackUri("forward:/fallback/products")
                        ).retry(retryConfig -> retryConfig.setRetries(5))
                         .requestRateLimiter(rl-> rl.setRateLimiter(redisRateLimiter()).setKeyResolver(hostNameKeyResolver()))
                        )  //f->f.circuitBreaker().retry().requestRateLimiter()    like this
                        .uri("lb://PRODUCT-MICROSERVICE"))
                .route("user-microservice", r -> r.path("/api/users/**","/message/**").uri("lb://USER-MICROSERVICE"))
                .route("order-microservice", r -> r.path("/api/orders/**", "/api/cart/**").uri("lb://ORDER-MICROSERVICE"))
                .route("eureka-server", r -> r.path("/eureka/main")
                        .filters(f -> f.rewritePath("/eureka/main", "/")).uri("http://localhost:8761"))

                .route("eureka-server-static", r -> r.path("/eureka/**").uri("http://localhost:8761"))
                .build();
    }
}


//Parameter	        Value	    Meaning
//replenishRate	    10	        Number of tokens to add per second (i.e. 10 requests/second allowed)
//burstCapacity	    20	        Max number of tokens that can be stored (i.e. burst up to 20 requests instantly)
//requestedTokens	1	        Number of tokens required per request (default is usually 1)