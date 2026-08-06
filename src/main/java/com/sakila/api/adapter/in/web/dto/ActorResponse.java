package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Actor appearing in a film")
public record ActorResponse(
        @Schema(description = "Actor identifier", example = "1")
        Integer id,
        @Schema(description = "First name of the actor", example = "PENELOPE")
        String firstName,
        @Schema(description = "Last name of the actor", example = "GUINESS")
        String lastName) {
}
