package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Availability summary for a film across all stores")
public record AvailabilityResponse(
        @Schema(description = "Identifier of the film", example = "1")
        Integer filmId,
        @Schema(description = "Title of the film", example = "ACADEMY DINOSAUR")
        String title,
        @Schema(description = "Total number of copies of the film", example = "5")
        long totalCopies,
        @Schema(description = "Number of copies with no active rental", example = "2")
        long availableCopies,
        @Schema(description = "Number of copies currently rented out", example = "3")
        long rentedCopies) {
}
