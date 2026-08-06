package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "City of an address")
public record CityResponse(
        @Schema(description = "City identifier", example = "300")
        Integer id,
        @Schema(description = "City name", example = "Lethbridge")
        String city,
        @Schema(description = "Country of the city")
        CountryResponse country) {
}
