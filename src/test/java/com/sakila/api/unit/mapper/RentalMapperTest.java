package com.sakila.api.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.sakila.api.adapter.in.web.dto.RentalResponse;
import com.sakila.api.domain.model.Rental;
import com.sakila.api.mapper.RentalMapper;

class RentalMapperTest {

    @Test
    void toResponseMarksActiveWhenReturnDateNull() {
        Rental active = new Rental(10, Instant.parse("2026-08-01T10:00:00Z"), null, 1, 5, "ACADEMY", 3);

        RentalResponse response = RentalMapper.toResponse(active);

        assertEquals(10, response.id());
        assertEquals(Instant.parse("2026-08-01T10:00:00Z"), response.rentalDate());
        assertNullResponse(response);
        assertEquals(1, response.customerId());
        assertEquals(5, response.inventoryId());
        assertEquals("ACADEMY", response.filmTitle());
        assertEquals(3, response.staffId());
        assertTrue(response.active());
    }

    @Test
    void toResponseMarksReturnedWhenReturnDatePresent() {
        Instant returned = Instant.parse("2026-08-05T12:00:00Z");
        Rental rental = new Rental(10, Instant.parse("2026-08-01T10:00:00Z"), returned, 1, 5, "ACADEMY", 3);

        RentalResponse response = RentalMapper.toResponse(rental);

        assertEquals(returned, response.returnDate());
        assertFalse(response.active());
    }

    private void assertNullResponse(RentalResponse response) {
        assertEquals(null, response.returnDate());
    }
}
