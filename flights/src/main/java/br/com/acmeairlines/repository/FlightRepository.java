package br.com.acmeairlines.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.acmeairlines.model.FlightModel;

public interface FlightRepository extends JpaRepository<FlightModel, Long> {
    Page<FlightModel> findAll(Pageable pageable);
    FlightModel findByTag(String tag);
}