package br.com.acmeairlines.users.dto;

import br.com.acmeairlines.users.model.Role;

public record UserUpdateDTO(String name, String email, String password, String currentPassword, String phone, AddressData address, Role role) {}
