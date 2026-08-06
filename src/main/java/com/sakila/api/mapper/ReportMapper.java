package com.sakila.api.mapper;

import com.sakila.api.adapter.in.web.dto.ActiveRentalsByStoreResponse;
import com.sakila.api.adapter.in.web.dto.MostRentedFilmResponse;
import com.sakila.api.adapter.in.web.dto.RentalsByMonthResponse;
import com.sakila.api.adapter.in.web.dto.RevenueByCategoryResponse;
import com.sakila.api.adapter.in.web.dto.TopCustomerResponse;
import com.sakila.api.domain.model.ActiveRentalsByStore;
import com.sakila.api.domain.model.MostRentedFilm;
import com.sakila.api.domain.model.RentalsByMonth;
import com.sakila.api.domain.model.RevenueByCategory;
import com.sakila.api.domain.model.TopCustomer;

public final class ReportMapper {

    private ReportMapper() {
    }

    public static MostRentedFilmResponse toMostRented(MostRentedFilm report) {
        return new MostRentedFilmResponse(report.filmId(), report.title(), report.timesRented());
    }

    public static TopCustomerResponse toTopCustomer(TopCustomer report) {
        return new TopCustomerResponse(report.customerId(), report.name(), report.rentalsCount());
    }

    public static RevenueByCategoryResponse toRevenue(RevenueByCategory report) {
        return new RevenueByCategoryResponse(report.category(), report.revenue());
    }

    public static RentalsByMonthResponse toRentalsByMonth(RentalsByMonth report) {
        return new RentalsByMonthResponse(report.month(), report.rentals());
    }

    public static ActiveRentalsByStoreResponse toActiveByStore(ActiveRentalsByStore report) {
        return new ActiveRentalsByStoreResponse(report.storeId(), report.activeCount());
    }
}
