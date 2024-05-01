package br.com.acmeairlines.users.dto;

public record UpdateUserDTO(
        String email,
        String phone,
        String password,
        AddressDTO address
) {}
