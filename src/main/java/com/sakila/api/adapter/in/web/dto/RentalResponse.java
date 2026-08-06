package com.sakila.api.adapter.in.web.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Film rental record returned by the API")
public record RentalResponse(
        @Schema(description = "Rental identifier", example = "1")
        Integer id,
        @Schema(description = "Date and time the rental started", example = "2026-08-05T10:30:00Z")
        Instant rentalDate,
        @Schema(description = "Date and time the film was returned. Null while the rental is active.", example = "2026-08-11T09:15:00Z")
        Instant returnDate,
        @Schema(description = "Identifier of the customer that rented the film", example = "1")
        Integer customerId,
        @Schema(description = "Identifier of the inventory copy that was rented", example = "1")
        Integer inventoryId,
        @Schema(description = "Title of the rented film", example = "ACADEMY DINOSAUR")
        String filmTitle,
        @Schema(description = "Identifier of the employee that handled the rental", example = "1")
        Integer staffId,
        @Schema(description = "True while the copy has not been returned", example = "true")
        boolean active) {
}
