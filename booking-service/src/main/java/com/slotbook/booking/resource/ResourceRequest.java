package com.slotbook.booking.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.UUID;

public record ResourceRequest(
        @NotBlank String name,
        @NotNull ResourceType type,
        @NotNull LocalTime openingTime,
        @NotNull LocalTime closingTime
){}

