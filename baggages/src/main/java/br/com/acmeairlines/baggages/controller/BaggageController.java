package br.com.acmeairlines.baggages.controller;

import br.com.acmeairlines.baggages.dto.BaggageDTO;
import br.com.acmeairlines.baggages.dto.CreateBaggageDTO;
import br.com.acmeairlines.baggages.service.BaggageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/baggages")
public class BaggageController {

    @Autowired
    private BaggageService baggageService;

    @PostMapping
    public ResponseEntity<BaggageDTO> createBaggage(@Valid @RequestBody CreateBaggageDTO createBaggageDTO) {
        BaggageDTO createdBaggage = baggageService.createBaggage(createBaggageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBaggage);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BaggageDTO>> getBaggagesByUserId(@PathVariable Long userId) {
        List<BaggageDTO> baggages = baggageService.getBaggagesByUserId(userId);
        return ResponseEntity.ok(baggages);
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<List<BaggageDTO>> getBaggagesByFlightId(@PathVariable Long flightId) {
        List<BaggageDTO> baggages = baggageService.getBaggagesByFlightId(flightId);
        return ResponseEntity.ok(baggages);
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<BaggageDTO> getBaggageByTag(@PathVariable String tag) {
        BaggageDTO baggage = baggageService.getBaggageByTag(tag);
        if (baggage != null) {
            return ResponseEntity.ok(baggage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tracker/{trackerUserId}")
    public ResponseEntity<List<BaggageDTO>> getBaggagesByTrackerUserId(@PathVariable Long trackerUserId) {
        List<BaggageDTO> baggages = baggageService.getBaggagesByTrackerUserId(trackerUserId);
        return ResponseEntity.ok(baggages);
    }

    @DeleteMapping("/{baggageId}")
    public ResponseEntity<Void> deleteBaggage(@PathVariable Long baggageId) {
        baggageService.deleteBaggage(baggageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{baggageId}")
    public ResponseEntity<BaggageDTO> updateBaggage(@PathVariable Long baggageId, @Valid @RequestBody BaggageDTO baggageDTO) {
        BaggageDTO updatedBaggage = baggageService.updateBaggage(baggageId, baggageDTO);
        return ResponseEntity.ok(updatedBaggage);
    }

}