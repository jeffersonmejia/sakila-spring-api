package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Physical copy of a film available in a store")
public record InventoryResponse(
        @Schema(description = "Inventory copy identifier", example = "1")
        Integer id,
        @Schema(description = "Identifier of the film the copy belongs to", example = "1")
        Integer filmId,
        @Schema(description = "Title of the film", example = "ACADEMY DINOSAUR")
        String filmTitle,
        @Schema(description = "Identifier of the store that holds the copy", example = "1")
        Integer storeId,
        @Schema(description = "True when the copy has no active rental and can be rented", example = "true")
        boolean available) {
}
