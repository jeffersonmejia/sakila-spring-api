package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Number of active rentals per store")
public record ActiveRentalsByStoreResponse(
        @Schema(description = "Identifier of the store", example = "1")
        Integer storeId,
        @Schema(description = "Number of copies currently rented out by the store", example = "67")
        Long activeCount) {
}
