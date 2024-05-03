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
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(WebClient.Builder webClientBuilder, ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.webClient = webClientBuilder
                .baseUrl("http://user-ms")
                .filter(lbFunction)
                .build();
    }

    public Mono<Boolean> validateToken(String token) {
        logger.info("Validando token: {}", token);
        return webClient.get()
                .uri("/users/validate")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnNext(isValid -> logger.info("Resultado da validação: {}", isValid))
                .onErrorResume(e -> {
                    logger.error("Erro ao validar o token", e);
                    return Mono.just(false);
                });
    }
}
