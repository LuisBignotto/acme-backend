package br.com.acmeairlines.apigateway.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-ms", path = "/users")
public interface TokenFeignClient {
    @GetMapping("/validate")
    boolean validateToken(@RequestHeader("Authorization") String token);
}
