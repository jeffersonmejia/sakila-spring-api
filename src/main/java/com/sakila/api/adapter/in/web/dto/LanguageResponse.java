package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Language in which a film is available")
public record LanguageResponse(
        @Schema(description = "Language identifier", example = "1")
        Integer id,
        @Schema(description = "Language name", example = "English")
        String name) {
}
