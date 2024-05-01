package br.com.acmeairlines.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(
        @Email
        @NotBlank
        String email,
        @NotBlank
        String cpf,
        @NotBlank
        String name,
        @NotBlank
        String password
) {}
