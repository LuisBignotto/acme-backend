package br.com.acmeairlines.users.service;

import br.com.acmeairlines.users.dto.AddressDTO;
import br.com.acmeairlines.users.dto.CreateUserDTO;
import br.com.acmeairlines.users.dto.UpdateUserDTO;
import br.com.acmeairlines.users.model.AddressModel;
import br.com.acmeairlines.users.model.UserModel;
import br.com.acmeairlines.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserModel createUser(CreateUserDTO createUserDTO) {
        UserModel user = new UserModel();
        user.setEmail(createUserDTO.email());
        user.setCpf(createUserDTO.cpf());
        user.setName(createUserDTO.name());
        user.setPassword(createUserDTO.password());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<UserModel> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<UserModel> getUserByCpf(String cpf) {
        return userRepository.findByCpf(cpf);
    }

    @Transactional
    public UserModel updateUser(Long id, UpdateUserDTO updateUserDTO) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setEmail(updateUserDTO.email());
        user.setPassword(updateUserDTO.password());
        user.setPhone(updateUserDTO.phone());

        updateAddresses(user, updateUserDTO.addressess());

        return userRepository.save(user);
    }

    private void updateAddresses(UserModel user, Set<AddressDTO> addressDTOs) {
        Set<AddressModel> updatedAddresses = new HashSet<>();
        for (AddressDTO dto : addressDTOs) {
            AddressModel address;
            if (dto.id() != null) {
                address = user.getAddresses().stream()
                        .filter(a -> a.getId().equals(dto.id()))
                        .findFirst()
                        .orElse(new AddressModel());
            } else {
                address = new AddressModel();
            }
            address.setStreet(dto.street());
            address.setNeighborhood(dto.neighborhood());
            address.setZipcode(dto.zipcode());
            address.setNumber(dto.number());
            address.setComplement(dto.complement());
            address.setCity(dto.city());
            address.setState(dto.state());
            address.setUser(user);
            updatedAddresses.add(address);
        }
        user.getAddresses().clear();
        user.getAddresses().addAll(updatedAddresses);
    }

    @Transactional
    public void deleteUser(Long id) {
        UserModel user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }
}
