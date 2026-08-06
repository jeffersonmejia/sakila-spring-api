package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Payload to register a new film rental")
public record RentalRequest(
        @Schema(description = "Identifier of an existing and active customer", example = "1")
        @NotNull(message = "El cliente es obligatorio") @Positive(message = "El cliente debe ser un entero positivo") Integer customerId,
        @Schema(description = "Identifier of an existing and available inventory copy", example = "1")
        @NotNull(message = "La copia es obligatoria") @Positive(message = "La copia debe ser un entero positivo") Integer inventoryId,
        @Schema(description = "Identifier of the employee registering the rental", example = "1")
        @NotNull(message = "El empleado es obligatorio") @Positive(message = "El empleado debe ser un entero positivo") Integer staffId) {
}
