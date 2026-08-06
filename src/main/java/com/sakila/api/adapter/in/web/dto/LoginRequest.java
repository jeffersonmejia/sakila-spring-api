package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Schema(description = "User name of the account to authenticate", example = "admin")
        @NotBlank(message = "El usuario es obligatorio") String username,
        @Schema(description = "Plain text password of the account. Minimum 8 characters.", example = "admin123")
        @NotBlank(message = "La contraseña es obligatoria") @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String password) {
}
