package br.com.acmeairlines.tickets.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "user-ms")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserDTO getUserById(@RequestHeader("Authorization") String token, @PathVariable("id") Long id);

    @GetMapping("/search")
    UserDTO getUserByEmail(@RequestHeader("Authorization") String token, @RequestParam("email") String email);

    @GetMapping("/users/check")
    Map<String, Object> checkToken(@RequestHeader("Authorization") String token);
}
