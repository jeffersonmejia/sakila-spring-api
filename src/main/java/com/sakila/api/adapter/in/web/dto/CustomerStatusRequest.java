package com.sakila.api.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;

public record CustomerStatusRequest(@NotNull(message = "El estado es obligatorio") Boolean active) {
}
