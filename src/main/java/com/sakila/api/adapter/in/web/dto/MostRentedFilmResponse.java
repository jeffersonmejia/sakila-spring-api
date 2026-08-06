package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Film ranked by how many times it has been rented")
public record MostRentedFilmResponse(
        @Schema(description = "Identifier of the film", example = "1")
        Integer filmId,
        @Schema(description = "Title of the film", example = "BUCKET BROTHERHOOD")
        String title,
        @Schema(description = "Number of times the film has been rented", example = "32")
        Long timesRented) {
}
