package com.sakila.api.mapper;

import com.sakila.api.adapter.in.web.dto.AvailabilityResponse;
import com.sakila.api.adapter.in.web.dto.InventoryResponse;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.Inventory;

public final class InventoryMapper {

    private InventoryMapper() {
    }

    public static InventoryResponse toResponse(Inventory inventory) {
        return new InventoryResponse(inventory.id(), inventory.filmId(), inventory.filmTitle(), inventory.storeId(),
                inventory.available());
    }

    public static AvailabilityResponse toAvailability(Film film, long totalCopies, long availableCopies) {
        return new AvailabilityResponse(film.id(), film.title(), totalCopies, availableCopies,
                totalCopies - availableCopies);
    }
}
