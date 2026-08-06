package com.sakila.api.adapter.in.web.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sakila.api.adapter.in.web.dto.ActiveRentalsByStoreResponse;
import com.sakila.api.adapter.in.web.dto.MostRentedFilmResponse;
import com.sakila.api.adapter.in.web.dto.RentalsByMonthResponse;
import com.sakila.api.adapter.in.web.dto.RevenueByCategoryResponse;
import com.sakila.api.adapter.in.web.dto.TopCustomerResponse;
import com.sakila.api.domain.port.in.ReportQuery;
import com.sakila.api.mapper.ReportMapper;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportQuery reportQuery;

    public ReportController(ReportQuery reportQuery) {
        this.reportQuery = reportQuery;
    }

    @GetMapping("/most-rented-films")
    public List<MostRentedFilmResponse> mostRentedFilms(@RequestParam(defaultValue = "10") int limit) {
        return reportQuery.mostRentedFilms(limit).stream().map(ReportMapper::toMostRented).toList();
    }

    @GetMapping("/top-customers")
    public List<TopCustomerResponse> topCustomers(@RequestParam(defaultValue = "10") int limit) {
        return reportQuery.topCustomers(limit).stream().map(ReportMapper::toTopCustomer).toList();
    }

    @GetMapping("/revenue-by-category")
    public List<RevenueByCategoryResponse> revenueByCategory() {
        return reportQuery.revenueByCategory().stream().map(ReportMapper::toRevenue).toList();
    }

    @GetMapping("/rentals-by-month")
    public List<RentalsByMonthResponse> rentalsByMonth() {
        return reportQuery.rentalsByMonth().stream().map(ReportMapper::toRentalsByMonth).toList();
    }

    @GetMapping("/active-rentals-by-store")
    public List<ActiveRentalsByStoreResponse> activeRentalsByStore() {
        return reportQuery.activeRentalsByStore().stream().map(ReportMapper::toActiveByStore).toList();
    }
}
