package com.slotbook.booking.resource;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.UUID;

public record ResourceRequest(
        @NotBlank String name,
        @NotNull ResourceType type,
        @NotNull LocalTime openingTime,
        @NotNull LocalTime closingTime)
{
    @AssertTrue(message = "openingTime must be before closingTime")
    public boolean isOpeningTimeBeforeClosingTime() {
        if (openingTime == null || closingTime == null) {
            return true;
        }
        return openingTime.isBefore(closingTime);
    }
}

