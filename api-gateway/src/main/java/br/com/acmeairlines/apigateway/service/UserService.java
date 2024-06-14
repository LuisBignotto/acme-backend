package br.com.acmeairlines.apigateway.service;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient.Builder webClientBuilder, ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.webClient = webClientBuilder
                .baseUrl("http://user-ms")
                .filter(lbFunction)
                .build();
    }

    public Mono<Boolean> validateToken(String token) {
        return webClient.get()
                .uri("/users/validate")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(e -> Mono.just(false));
    }

    public Mono<Map<String, Object>> checkToken(String token) {
        return webClient.get()
                .uri("/users/check")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .onErrorResume(e -> Mono.empty());
    }
}
