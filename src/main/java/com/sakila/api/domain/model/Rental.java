package com.sakila.api.domain.model;

import java.time.Instant;

public record Rental(
        Integer id,
        Instant rentalDate,
        Instant returnDate,
        Integer customerId,
        Integer inventoryId,
        String filmTitle,
        Integer staffId) {
}
