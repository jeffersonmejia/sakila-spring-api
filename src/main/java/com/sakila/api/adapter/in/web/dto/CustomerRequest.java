package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to register a new customer")
public record CustomerRequest(
        @Schema(description = "First name of the customer. Maximum 45 characters.", example = "MARY")
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 45, message = "El nombre debe tener máximo 45 caracteres") String firstName,
        @Schema(description = "Last name of the customer. Maximum 45 characters.", example = "SMITH")
        @NotBlank(message = "El apellido es obligatorio") @Size(max = 45, message = "El apellido debe tener máximo 45 caracteres") String lastName,
        @Schema(description = "Unique email address of the customer. Must not be already registered.", example = "mary.smith@sakila.com")
        @NotBlank(message = "El correo electrónico es obligatorio") @Email(message = "El correo electrónico no es válido") @Size(max = 50, message = "El correo electrónico debe tener máximo 50 caracteres") String email,
        @Schema(description = "Identifier of the store the customer belongs to", example = "1")
        @NotNull(message = "La tienda es obligatoria") Integer storeId,
        @Schema(description = "Identifier of an existing address assigned to the customer", example = "1")
        @NotNull(message = "La dirección es obligatoria") Integer addressId) {
}
