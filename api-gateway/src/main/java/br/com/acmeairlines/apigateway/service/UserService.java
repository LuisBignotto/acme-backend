package br.com.acmeairlines.apigateway.service;

import br.com.acmeairlines.apigateway.feignclient.TokenFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final TokenFeignClient tokenFeignClient;

    @Autowired
    public UserService(@Lazy TokenFeignClient tokenFeignClient) {
        this.tokenFeignClient = tokenFeignClient;
    }

    public boolean validateToken(String token) {
        return tokenFeignClient.validateToken(token);
    }
}
