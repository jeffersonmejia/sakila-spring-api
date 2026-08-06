package com.sakila.api.adapter.in.web.dto;

import java.time.Instant;

public record RentalResponse(
        Integer id,
        Instant rentalDate,
        Instant returnDate,
        Integer customerId,
        Integer inventoryId,
        String filmTitle,
        Integer staffId,
        boolean active) {
}
