package br.com.acmeairlines.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record FlightCreateDTO(
    @NotBlank String tag,
    @NotBlank LocalDateTime departureDate,
    @NotBlank LocalDateTime arrivalDate,
    @NotBlank String departureAirport,
    @NotBlank String arrivalAirport,
    @NotBlank String status,
    @NotBlank String airplaneModel
) {}
