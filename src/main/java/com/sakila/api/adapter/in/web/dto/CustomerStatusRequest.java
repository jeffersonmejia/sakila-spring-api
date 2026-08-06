package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload to activate or deactivate a customer")
public record CustomerStatusRequest(
        @Schema(description = "New status of the customer. `true` activates, `false` deactivates.", example = "false")
        @NotNull(message = "El estado es obligatorio") Boolean active) {
}
