package br.com.acmeairlines.baggages.dto;

public record BaggageTrackerDTO(
        Long id,
        Long baggageId,
        Long trackerUserId
) {}
