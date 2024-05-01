package br.com.acmeairlines.users.dto;

import java.util.List;

public record UpdateUserDTO(
        String email,
        String phone,
        String password,
        List<AddressDTO> addressess
) {}
