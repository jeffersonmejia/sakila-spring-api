package com.sakila.api.adapter.in.web.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Paged result envelope wrapping a list of items together with pagination metadata")
public record PageResponse<T>(
        @Schema(description = "Items of the current page")
        List<T> content,
        @Schema(description = "Zero based index of the returned page", example = "0")
        int page,
        @Schema(description = "Maximum number of items per page", example = "20")
        int size,
        @Schema(description = "Total number of items across all pages", example = "1000")
        long totalElements,
        @Schema(description = "Total number of pages", example = "50")
        int totalPages,
        @Schema(description = "True when this is the first page", example = "true")
        boolean first,
        @Schema(description = "True when this is the last page", example = "false")
        boolean last) {
}
