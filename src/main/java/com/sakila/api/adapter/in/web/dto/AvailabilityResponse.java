package com.sakila.api.adapter.in.web.dto;

public record AvailabilityResponse(Integer filmId, String title, long totalCopies, long availableCopies,
        long rentedCopies) {
}
