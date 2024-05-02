package br.com.acmeairlines.apigateway.config;

import br.com.acmeairlines.apigateway.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-ms", r -> r.path("/user-ms/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://user-ms"))
                .route("flight-ms", r -> r.path("/flight-ms/**")
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://flight-ms"))
                .route("baggage-ms", r -> r.path("/baggage-ms/**")
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://baggage-ms"))
                .build();
    }
}
