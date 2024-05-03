package br.com.acmeairlines.flights.repository;

import br.com.acmeairlines.flights.model.FlightModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlightRepository extends JpaRepository<FlightModel, Long> {
    Page<FlightModel> findAll(Pageable pageable);
    Optional<FlightModel> findByTag(String tag);
}