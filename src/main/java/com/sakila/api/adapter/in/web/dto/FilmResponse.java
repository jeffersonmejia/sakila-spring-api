package com.sakila.api.adapter.in.web.dto;

import java.math.BigDecimal;

public record FilmResponse(
        Integer id,
        String title,
        String description,
        Integer releaseYear,
        String language,
        Short rentalDuration,
        BigDecimal rentalRate,
        Short length,
        BigDecimal replacementCost,
        String rating) {
}
