package com.sakila.api.mapper;

import com.sakila.api.adapter.in.web.dto.RentalResponse;
import com.sakila.api.domain.model.Rental;

public final class RentalMapper {

    private RentalMapper() {
    }

    public static RentalResponse toResponse(Rental rental) {
        return new RentalResponse(rental.id(), rental.rentalDate(), rental.returnDate(), rental.customerId(),
                rental.inventoryId(), rental.filmTitle(), rental.staffId(), rental.returnDate() == null);
    }
}
