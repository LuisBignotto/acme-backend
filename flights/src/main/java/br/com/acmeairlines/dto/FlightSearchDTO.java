package br.com.acmeairlines.dto;

import java.time.LocalDateTime;

public record FlightSearchDTO(
    LocalDateTime departureDate,
    LocalDateTime arrivalDate,
    String departureAirport,
    String arrivalAirport,
    String status
) {}