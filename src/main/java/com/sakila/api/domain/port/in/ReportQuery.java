package com.sakila.api.domain.port.in;

import java.util.List;

import com.sakila.api.domain.model.ActiveRentalsByStore;
import com.sakila.api.domain.model.MostRentedFilm;
import com.sakila.api.domain.model.RentalsByMonth;
import com.sakila.api.domain.model.RevenueByCategory;
import com.sakila.api.domain.model.TopCustomer;

public interface ReportQuery {

    List<MostRentedFilm> mostRentedFilms(int limit);

    List<TopCustomer> topCustomers(int limit);

    List<RevenueByCategory> revenueByCategory();

    List<RentalsByMonth> rentalsByMonth();

    List<ActiveRentalsByStore> activeRentalsByStore();
}
