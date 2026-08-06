package com.sakila.api.adapter.in.web.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Summary of a film returned by list and search operations")
public record FilmResponse(
        @Schema(description = "Film identifier", example = "1")
        Integer id,
        @Schema(description = "Title of the film", example = "ACADEMY DINOSAUR")
        String title,
        @Schema(description = "Short plot summary", example = "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Student in The Russian Moscow")
        String description,
        @Schema(description = "Year the film was released", example = "2006")
        Integer releaseYear,
        @Schema(description = "Language of the film", example = "English")
        String language,
        @Schema(description = "Rental duration in days", example = "6")
        Short rentalDuration,
        @Schema(description = "Cost to rent the film for the rental duration", example = "0.99")
        BigDecimal rentalRate,
        @Schema(description = "Film length in minutes", example = "86")
        Short length,
        @Schema(description = "Cost charged if the film is lost or badly damaged", example = "20.99")
        BigDecimal replacementCost,
        @Schema(description = "MPAA rating. One of `G`, `PG`, `PG-13`, `R`, `NC-17`.", example = "PG")
        String rating) {
}
