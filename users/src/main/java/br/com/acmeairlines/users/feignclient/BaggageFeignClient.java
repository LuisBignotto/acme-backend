package br.com.acmeairlines.users.feignclient;

import br.com.acmeairlines.users.dto.BaggageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "baggage-ms")
public interface BaggageFeignClient {

    @GetMapping("/baggages/user/{userId}")
    ResponseEntity<List<BaggageDTO>> getBaggagesByUserId(@PathVariable Long userId);

}
