package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Country of a city")
public record CountryResponse(
        @Schema(description = "Country identifier", example = "103")
        Integer id,
        @Schema(description = "Country name", example = "Canada")
        String country) {
}
