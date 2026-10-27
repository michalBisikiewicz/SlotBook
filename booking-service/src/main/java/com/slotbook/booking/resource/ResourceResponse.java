package com.slotbook.booking.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.UUID;

public record ResourceResponse (
     UUID id,
     String name,
     ResourceType type,
     LocalTime openingTime,
     LocalTime closingTime,
    boolean active
){}
