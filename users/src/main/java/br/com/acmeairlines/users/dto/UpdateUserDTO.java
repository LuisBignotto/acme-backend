package br.com.acmeairlines.users.dto;

import java.util.Set;

public record UpdateUserDTO(
        String email,
        String phone,
        String password,
        Set<AddressDTO> addressess
) {}
