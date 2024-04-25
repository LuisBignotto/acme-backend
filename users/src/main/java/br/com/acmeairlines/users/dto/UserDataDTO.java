package br.com.acmeairlines.users.dto;

import br.com.acmeairlines.users.model.Address;
import br.com.acmeairlines.users.model.Role;
import br.com.acmeairlines.users.model.UserModel;

public record UserDataDTO(String id, String name, String email, String cpf, String phone, Role role, Address address) {
    // Devolve um dto do usermodel sem o campo senha
    public UserDataDTO(UserModel user){
        this(user.getId(), user.getName(), user.getEmail(), user.getCpf(), user.getPhone(), user.getRole(), user.getAddress());
    }
}
