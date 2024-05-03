package br.com.acmeairlines.baggages.dto;

import jakarta.validation.constraints.NotNull;

public record CreateBaggageTrackerDTO(
        @NotNull Long trackerUserId
) {}
