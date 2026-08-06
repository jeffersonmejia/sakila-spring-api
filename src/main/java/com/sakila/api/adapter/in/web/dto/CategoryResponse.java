package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Film category")
public record CategoryResponse(
        @Schema(description = "Category identifier", example = "1")
        Integer id,
        @Schema(description = "Category name", example = "Action")
        String name) {
}
