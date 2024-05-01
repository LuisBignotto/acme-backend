package br.com.acmeairlines.users.repository;

import br.com.acmeairlines.users.dto.UserDTO;
import br.com.acmeairlines.users.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, String> {
    Page<UserDTO> findByActive(Boolean active, Pageable pageable);

    UserDTO findByEmail(String email);

    UserDTO findByCpf(String cpf);
}
