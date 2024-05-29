package br.com.acmeairlines.apigateway.service;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
}
