package br.com.acmeairlines.flights.dto;

import java.time.LocalDateTime;

public record FlightUpdateDTO(
    String tag,
    LocalDateTime departureDate,
    LocalDateTime arrivalDate,
    String departureAirport,
    String arrivalAirport,
    String status,
    String airplaneModel
) {}
