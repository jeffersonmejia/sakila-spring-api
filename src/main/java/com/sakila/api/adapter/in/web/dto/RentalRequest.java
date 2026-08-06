package com.sakila.api.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RentalRequest(
        @NotNull(message = "El cliente es obligatorio") @Positive(message = "El cliente debe ser un entero positivo") Integer customerId,
        @NotNull(message = "La copia es obligatoria") @Positive(message = "La copia debe ser un entero positivo") Integer inventoryId,
        @NotNull(message = "El empleado es obligatorio") @Positive(message = "El empleado debe ser un entero positivo") Integer staffId) {
}
