package com.sakila.api.adapter.in.web.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Customer information returned by the API")
public record CustomerResponse(
        @Schema(description = "Customer identifier", example = "1")
        Integer id,
        @Schema(description = "First name of the customer", example = "MARY")
        String firstName,
        @Schema(description = "Last name of the customer", example = "SMITH")
        String lastName,
        @Schema(description = "Email address of the customer", example = "mary.smith@sakila.com")
        String email,
        @Schema(description = "Whether the customer can rent films", example = "true")
        Boolean active,
        @Schema(description = "Identifier of the store the customer belongs to", example = "1")
        Integer storeId,
        @Schema(description = "Date the customer was registered", example = "2026-08-05")
        LocalDate createDate,
        @Schema(description = "Postal address of the customer")
        AddressResponse address) {
}
