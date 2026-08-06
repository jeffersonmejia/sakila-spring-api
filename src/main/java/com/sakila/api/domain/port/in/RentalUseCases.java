package com.sakila.api.domain.port.in;

import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.Rental;

public interface RentalUseCases {

    Rental createRental(Rental rental);

    Rental getRental(Integer id);

    PageResult<Rental> listActive(PageQuery query);

    PageResult<Rental> listOverdue(PageQuery query);

    PageResult<Rental> customerRentals(Integer customerId, PageQuery query);

    Rental returnRental(Integer id);
}
