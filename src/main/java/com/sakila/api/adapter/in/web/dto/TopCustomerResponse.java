package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Customer ranked by the number of rentals")
public record TopCustomerResponse(
        @Schema(description = "Identifier of the customer", example = "1")
        Integer customerId,
        @Schema(description = "Full name of the customer", example = "MARY SMITH")
        String name,
        @Schema(description = "Number of rentals made by the customer", example = "46")
        Long rentalsCount) {
}
