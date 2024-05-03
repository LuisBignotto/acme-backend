package br.com.acmeairlines.baggages.repository;

import br.com.acmeairlines.baggages.model.BaggageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaggageRepository extends JpaRepository<BaggageModel, Long> {
    List<BaggageModel> findByUserId(Long userId);

    List<BaggageModel> findByFlightId(Long id);

    BaggageModel findByTag(String tag);
}
