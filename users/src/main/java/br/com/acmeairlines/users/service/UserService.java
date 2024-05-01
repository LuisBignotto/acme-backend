package br.com.acmeairlines.users.service;

import br.com.acmeairlines.users.dto.AddressDTO;
import br.com.acmeairlines.users.dto.CreateUserDTO;
import br.com.acmeairlines.users.dto.UpdateUserDTO;
import br.com.acmeairlines.users.model.AddressModel;
import br.com.acmeairlines.users.model.UserModel;
import br.com.acmeairlines.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserModel createUser(CreateUserDTO createUserDTO) {
        UserModel user = new UserModel();
        user.setEmail(createUserDTO.email());
        user.setCpf(createUserDTO.cpf());
        user.setName(createUserDTO.name());
        user.setPassword(passwordEncoder.encode(createUserDTO.password()));
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
        UserModel user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (updateUserDTO.email() != null) {
            user.setEmail(updateUserDTO.email());
        }

        if (updateUserDTO.password() != null) {
            user.setPassword(passwordEncoder.encode(updateUserDTO.password()));
        }

        if (updateUserDTO.phone() != null) {
            user.setPhone(updateUserDTO.phone());
        }

        AddressDTO addressDTO = updateUserDTO.address();

        if (addressDTO != null) {
            AddressModel address = user.getAddress();
            if (address == null) {
                address = new AddressModel();
                address.setUser(user);
                user.setAddress(address);
            }

            if(addressDTO.street() != null){
                address.setStreet(addressDTO.street());
            }

            if(addressDTO.neighborhood() != null){
                address.setNeighborhood(addressDTO.neighborhood());
            }

            if(addressDTO.zipcode() != null){
                address.setZipcode(addressDTO.zipcode());
            }

            if(addressDTO.number() != null){
                address.setNumber(addressDTO.number());
            }

            if(addressDTO.complement() != null){
                address.setComplement(addressDTO.complement());
            }

            if(addressDTO.city() != null){
                address.setCity(addressDTO.city());
            }

            if(addressDTO.state() != null){
                address.setState(addressDTO.state());
            }
        }

        return userRepository.save(user);
    }


    @Transactional
    public void deleteUser(Long id) {
        UserModel user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }
}
