package br.com.acmeairlines.users.dto;

import java.util.List;

public record UserResponseDTO (
        Long id,
        String email,
        String cpf,
        String name,
        String phone,
        AddressDTO address,
        List<String> roles,
        List<BaggageDTO> baggages
) {}