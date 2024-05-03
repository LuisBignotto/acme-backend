package br.com.acmeairlines.baggages.repository;

import br.com.acmeairlines.baggages.model.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<StatusModel, Long> {
}
