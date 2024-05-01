package br.com.acmeairlines.users.service;

import br.com.acmeairlines.users.dto.UserDTO;
import br.com.acmeairlines.users.model.Address;
import br.com.acmeairlines.users.model.UserModel;
import br.com.acmeairlines.users.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public UserModel createUser(@Valid UserRegisterDTO data) {
        if (repository.findByEmail(data.email()) != null) {
            throw new IllegalArgumentException("Email already in use.");
        }

        if (repository.findByCpf(data.cpf()) != null) {
            throw new IllegalArgumentException("CPF already in use.");
        }

        if (data.password().length() < 8) {
            throw new IllegalArgumentException("Password too short.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserModel newUser = new UserModel(UUID.randomUUID().toString(), data.name(), data.email(), data.cpf(), encryptedPassword, null, data.active(), null, data.role());
        return repository.save(newUser);
    }

    public UserDataDTO updateUser(@Valid UserUpdateDTO data, String userId) {
        UserModel user = repository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (data.currentPassword() != null) {
            boolean isCurrentPasswordValid = passwordEncoder.matches(data.currentPassword(), user.getPassword());
            if (!isCurrentPasswordValid) {
                throw new IllegalArgumentException("Current password is incorrect.");
            }
        }

        if (data.name() != null) {
            user.setName(data.name());
        }

        if (data.email() != null) {
            user.setEmail(data.email());
        }

        if (data.password() != null && data.password().length() > 8) {
            String encryptedPassword = passwordEncoder.encode(data.password());
            user.setPassword(encryptedPassword);
        }

        if (data.phone() != null) {
            user.setPhone(data.phone());
        }

        if (data.address() != null) {
            if (user.getAddress() == null) {
                user.setAddress(new Address(data.address()));
            } else {
                user.getAddress().updateAddress(data.address());
            }
        }

        if (data.role() != null) {
            user.setRole(data.role());
        }

        UserModel savedUser = repository.save(user);

        return new UserDataDTO(savedUser);
    }

    public UserDataDTO findByEmail(String email) {
        UserModel user = repository.findByEmail(email);
        if (user != null) {
            return new UserDataDTO(user);
        }
        return null;
    }

    public UserDataDTO findByCpf(String cpf) {
        UserModel user = repository.findByCpf(cpf);
        if (user != null) {
            return new UserDataDTO(user);
        }
        return null;
    }

    public void deleteUser(String id) {
        repository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found."));
        repository.deleteById(id);
    }

    public Page<UserDTO> findUsers(Pageable pages) {
        return repository.findByActive(true, pages)
                .map(UserDTO::new);
    }

}
