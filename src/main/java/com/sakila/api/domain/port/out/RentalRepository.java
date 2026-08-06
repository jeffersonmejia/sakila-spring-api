package com.sakila.api.domain.port.out;

import java.time.Instant;
import java.util.Optional;

import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.Rental;

public interface RentalRepository {

    Optional<Rental> findById(Integer id);

    PageResult<Rental> findByCustomerId(Integer customerId, PageQuery query);

    PageResult<Rental> findActive(PageQuery query);

    PageResult<Rental> findOverdue(PageQuery query);

    boolean hasActiveRental(Integer inventoryId);

    Rental save(Rental rental);

    Rental markReturned(Integer rentalId, Instant returnDate);
}
