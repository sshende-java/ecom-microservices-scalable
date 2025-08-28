package com.ecommerce.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Java based Routing config instead of yml , beneficial for adding filters or modifying request itself
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-microservice", r -> r.path("/api/products/**")
                        .filters(f->f.circuitBreaker(config -> config.setName("ecomCircuitBreaker")
                                                    .setFallbackUri("forward:/fallback/products")
                                                    ).retry(retryConfig -> retryConfig.setRetries(5)))  //f->f.circuitBreaker().retry()     like this
                        .uri("lb://PRODUCT-MICROSERVICE"))
                .route("user-microservice", r -> r.path("/api/users/**").uri("lb://USER-MICROSERVICE"))
                .route("order-microservice", r -> r.path("/api/orders/**", "/api/cart/**").uri("lb://ORDER-MICROSERVICE"))
                .route("eureka-server", r -> r.path("/eureka/main")
                        .filters(f -> f.rewritePath("/eureka/main", "/")).uri("http://localhost:8761"))

                .route("eureka-server-static", r -> r.path("/eureka/**").uri("http://localhost:8761"))
                .build();
    }
}