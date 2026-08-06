package com.sakila.api.domain.model;

import java.time.LocalDateTime;

public record Rental(
        Integer id,
        LocalDateTime rentalDate,
        LocalDateTime returnDate,
        Integer customerId,
        Integer inventoryId,
        String filmTitle,
        Integer staffId) {
}
