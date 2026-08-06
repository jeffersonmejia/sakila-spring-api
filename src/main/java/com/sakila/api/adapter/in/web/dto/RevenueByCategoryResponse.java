package com.sakila.api.adapter.in.web.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Total revenue grouped by film category")
public record RevenueByCategoryResponse(
        @Schema(description = "Name of the film category", example = "Sports")
        String category,
        @Schema(description = "Accumulated revenue of the category", example = "5312.75")
        BigDecimal revenue) {
}
