package br.com.acmeairlines.apigateway.config;

import br.com.acmeairlines.apigateway.filter.AuthenticationFilter;
import br.com.acmeairlines.apigateway.filter.CorsResponseFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Lazy;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter authenticationFilter;
    private final CorsResponseFilter corsResponseFilter;

    public GatewayConfig(@Lazy AuthenticationFilter authenticationFilter, CorsResponseFilter corsResponseFilter) {
        this.authenticationFilter = authenticationFilter;
        this.corsResponseFilter = corsResponseFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-ms", r -> r.path("/user-ms/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://user-ms"))
                .route("flight-ms", r -> r.path("/flight-ms/**")
                        .filters(f -> f.filter(corsResponseFilter).filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://flight-ms"))
                .route("baggage-ms", r -> r.path("/baggage-ms/**")
                        .filters(f -> f.filter(corsResponseFilter).filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://baggage-ms"))
                .route("ticket-ms", r -> r.path("/ticket-ms/**")
                        .filters(f -> f.filter(corsResponseFilter).filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://ticket-ms"))
                .build();
    }
}
