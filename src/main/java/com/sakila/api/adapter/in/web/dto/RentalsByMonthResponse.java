package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Number of rentals per month")
public record RentalsByMonthResponse(
        @Schema(description = "Year and month in `yyyy-MM` format", example = "2006-05")
        String month,
        @Schema(description = "Number of rentals registered in the month", example = "123")
        Long rentals) {
}
