package com.sakila.api.adapter.in.web.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Full detail of a film including its cast and categories")
public record FilmDetailResponse(
        @Schema(description = "Film summary information")
        FilmResponse film,
        @Schema(description = "Actors that appear in the film")
        List<ActorResponse> actors,
        @Schema(description = "Categories the film belongs to")
        List<CategoryResponse> categories) {
}
