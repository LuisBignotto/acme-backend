package br.com.acmeairlines.users.dto;

public record BaggageTrackerDTO(
        Long id,
        Long baggageId,
        Long trackerUserId
) {}
