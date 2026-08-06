package com.sakila.api.domain.model;

public record Availability(Film film, long totalCopies, long availableCopies) {
}
