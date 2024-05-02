package br.com.acmeairlines.auth.feignclients;

import br.com.acmeairlines.auth.model.UserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Component
@FeignClient(name = "user-ms", path = "/users")
public interface UserFeignClient {

    @GetMapping("/search")
    ResponseEntity<UserModel> getUserByEmail(@RequestParam String email);
}
