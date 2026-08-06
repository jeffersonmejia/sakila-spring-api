package com.sakila.api.adapter.in.web.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Uniform error body returned by the API on any failed request")
public record ErrorResponse(
        @Schema(description = "Server time when the error occurred", example = "2026-08-05T15:30:00.123")
        LocalDateTime timestamp,
        @Schema(description = "HTTP status code", example = "404")
        int status,
        @Schema(description = "Standard HTTP reason phrase for the status", example = "Not Found")
        String error,
        @Schema(description = "Human readable description of the problem", example = "Film with id 9999 not found")
        String message,
        @Schema(description = "Request path that produced the error", example = "/api/films/9999")
        String path,
        @Schema(description = "Field-level validation messages. Only present on 400 validation errors.",
                example = "{\"email\":\"El correo electrónico no es válido\"}")
        Map<String, String> fields) {
}
