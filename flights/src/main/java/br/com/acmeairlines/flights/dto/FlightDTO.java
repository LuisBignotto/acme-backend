package br.com.acmeairlines.flights.dto;

import java.time.LocalDateTime;

public record FlightDTO(
    Long id,
    String tag,
    LocalDateTime departureDate,
    LocalDateTime arrivalDate,
    String departureAirport,
    String arrivalAirport,
    String status,
    String airplaneModel
) {}
