package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Postal address of a customer")
public record AddressResponse(
        @Schema(description = "Address identifier", example = "1")
        Integer id,
        @Schema(description = "First address line", example = "47 MySakila Drive")
        String address,
        @Schema(description = "District or province", example = "Alberta")
        String district,
        @Schema(description = "Postal code. Can be empty.", example = "")
        String postalCode,
        @Schema(description = "Phone number. Can be empty.", example = "")
        String phone,
        @Schema(description = "City of the address")
        CityResponse city) {
}
