package com.sakila.api.domain.model;

import java.math.BigDecimal;

public record Film(
        Integer id,
        String title,
        String description,
        Integer releaseYear,
        Language language,
        Short rentalDuration,
        BigDecimal rentalRate,
        Short length,
        BigDecimal replacementCost,
        String rating) {
}
