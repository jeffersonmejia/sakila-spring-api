package com.sakila.api.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.sakila.api.adapter.in.web.dto.AvailabilityResponse;
import com.sakila.api.adapter.in.web.dto.InventoryResponse;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.Inventory;
import com.sakila.api.domain.model.Language;
import com.sakila.api.mapper.InventoryMapper;

class InventoryMapperTest {

    @Test
    void toResponseMapsAllFields() {
        Inventory inventory = new Inventory(7, 3, "JERICHO MULAN", 2, true);

        InventoryResponse response = InventoryMapper.toResponse(inventory);

        assertEquals(7, response.id());
        assertEquals(3, response.filmId());
        assertEquals("JERICHO MULAN", response.filmTitle());
        assertEquals(2, response.storeId());
        assertEquals(true, response.available());
    }

    @Test
    void toAvailabilityComputesRentedCopies() {
        Film film = new Film(3, "JERICHO MULAN", "desc", 2006, new Language(1, "English"), (short) 6,
                java.math.BigDecimal.ONE, (short) 86, java.math.BigDecimal.TEN, "PG");

        AvailabilityResponse response = InventoryMapper.toAvailability(film, 5, 2);

        assertEquals(3, response.filmId());
        assertEquals("JERICHO MULAN", response.title());
        assertEquals(5, response.totalCopies());
        assertEquals(2, response.availableCopies());
        assertEquals(3, response.rentedCopies());
    }
}
