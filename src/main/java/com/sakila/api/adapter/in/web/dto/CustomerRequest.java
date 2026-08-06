package com.sakila.api.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 45, message = "El nombre debe tener máximo 45 caracteres") String firstName,
        @NotBlank(message = "El apellido es obligatorio") @Size(max = 45, message = "El apellido debe tener máximo 45 caracteres") String lastName,
        @NotBlank(message = "El correo electrónico es obligatorio") @Email(message = "El correo electrónico no es válido") @Size(max = 50, message = "El correo electrónico debe tener máximo 50 caracteres") String email,
        @NotNull(message = "La tienda es obligatoria") Integer storeId,
        @NotNull(message = "La dirección es obligatoria") Integer addressId) {
}
