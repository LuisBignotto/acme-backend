package br.com.acmeairlines.users.service;

import br.com.acmeairlines.users.dto.*;
import br.com.acmeairlines.users.feignclient.BaggageFeignClient;
import br.com.acmeairlines.users.model.AddressModel;
import br.com.acmeairlines.users.model.RoleModel;
import br.com.acmeairlines.users.model.UserModel;
import br.com.acmeairlines.users.repository.RoleRepository;
import br.com.acmeairlines.users.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private BaggageFeignClient baggageFeignClient;

    @Value("${jwt.token}")
    private String secret;

    public String generateToken(UserModel user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("acme-auth-api")
                    .withSubject(String.valueOf(user.getId()))
                    .withClaim("roles", user.getRoles().stream()
                            .map(RoleModel::getRoleName)
                            .collect(Collectors.toList()))
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public Map<String, Object> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("acme-auth-api")
                    .build()
                    .verify(token);
            Map<String, Object> tokenDetails = new HashMap<>();
            tokenDetails.put("id", decodedJWT.getSubject());
            tokenDetails.put("roles", decodedJWT.getClaim("roles").asList(String.class));
            return tokenDetails;
        } catch (JWTVerificationException exception) {
            return Collections.singletonMap("error", "invalid");
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-03:00"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    @Transactional
    public UserDTO createUser(@Valid CreateUserDTO createUserDTO) {

        Optional<UserModel> check_email = userRepository.findByEmail(createUserDTO.email());

        if(check_email.isPresent()){
            throw new IllegalArgumentException("Email already in use.");
        }

        Optional<UserModel> check_cpf = userRepository.findByEmail(createUserDTO.cpf());

        if(check_cpf.isPresent()){
            throw new IllegalArgumentException("CPF already in use.");
        }

        UserModel user = new UserModel();
        user.setEmail(createUserDTO.email());
        user.setCpf(createUserDTO.cpf());
        user.setName(createUserDTO.name());
        user.setPassword(passwordEncoder.encode(createUserDTO.password()));

        RoleModel defaultRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseGet(() -> {
                    RoleModel newRole = new RoleModel();
                    newRole.setRoleName("ROLE_USER");
                    return roleRepository.save(newRole);
                });

        user.getRoles().add(defaultRole);

        UserModel savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public Page<UserDTO> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return convertToDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByCpf(String cpf) {
        UserModel user = userRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("User not found with cpf: " + cpf));
        return convertToDTO(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
        UserModel user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (updateUserDTO.name() != null) {
            user.setName(updateUserDTO.name());
        }

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

            if (addressDTO.street() != null) {
                address.setStreet(addressDTO.street());
            }

            if (addressDTO.neighborhood() != null) {
                address.setNeighborhood(addressDTO.neighborhood());
            }

            if (addressDTO.zipcode() != null) {
                address.setZipcode(addressDTO.zipcode());
            }

            if (addressDTO.number() != null) {
                address.setNumber(addressDTO.number());
            }

            if (addressDTO.complement() != null) {
                address.setComplement(addressDTO.complement());
            }

            if (addressDTO.city() != null) {
                address.setCity(addressDTO.city());
            }

            if (addressDTO.state() != null) {
                address.setState(addressDTO.state());
            }
        }

        UserModel updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        UserModel user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public UserResponseDTO getCompleteUserInfoByEmail(String email) {
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email))
                .getId();
        return getCompleteUserInfo(userId);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getCompleteUserInfo(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        UserDTO userDTO = convertToDTO(user);

        ResponseEntity<List<BaggageDTO>> baggageResponse = baggageFeignClient.getBaggagesByUserId(userId);
        List<BaggageDTO> baggages = baggageResponse.getBody();

        return new UserResponseDTO(
                userDTO.id(),
                userDTO.email(),
                userDTO.cpf(),
                userDTO.name(),
                userDTO.phone(),
                userDTO.address(),
                userDTO.roles(),
                baggages
        );
    }

    @Transactional
    public UserDTO addRoleToUser(Long userId, String roleName) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        RoleModel role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));

        user.getRoles().add(role);
        UserModel updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Transactional
    public UserDTO removeRoleFromUser(Long userId, String roleName) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        RoleModel role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));

        user.getRoles().remove(role);
        UserModel updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    private UserDTO convertToDTO(UserModel user) {
        AddressDTO addressDTO = null;
        if (user.getAddress() != null) {
            AddressModel address = user.getAddress();
            addressDTO = new AddressDTO(
                    address.getId(),
                    address.getStreet(),
                    address.getNeighborhood(),
                    address.getZipcode(),
                    address.getNumber(),
                    address.getComplement(),
                    address.getCity(),
                    address.getState()
            );
        }

        List<String> roles = user.getRoles().stream()
                .map(RoleModel::getRoleName)
                .collect(Collectors.toList());

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getCpf(),
                user.getName(),
                user.getPhone(),
                addressDTO,
                roles
        );
    }

}
