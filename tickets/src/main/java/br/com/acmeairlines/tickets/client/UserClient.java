package br.com.acmeairlines.tickets.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-ms")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}