package com.sakila.api.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sakila.api.domain.model.ActiveRentalsByStore;
import com.sakila.api.domain.model.MostRentedFilm;
import com.sakila.api.domain.model.RentalsByMonth;
import com.sakila.api.domain.model.RevenueByCategory;
import com.sakila.api.domain.model.TopCustomer;
import com.sakila.api.domain.port.in.ReportQuery;
import com.sakila.api.domain.port.out.ReportRepository;

@Service
public class ReportService implements ReportQuery {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public List<MostRentedFilm> mostRentedFilms(int limit) {
        return reportRepository.mostRentedFilms(limit);
    }

    @Override
    public List<TopCustomer> topCustomers(int limit) {
        return reportRepository.topCustomers(limit);
    }

    @Override
    public List<RevenueByCategory> revenueByCategory() {
        return reportRepository.revenueByCategory();
    }

    @Override
    public List<RentalsByMonth> rentalsByMonth() {
        return reportRepository.rentalsByMonth();
    }

    @Override
    public List<ActiveRentalsByStore> activeRentalsByStore() {
        return reportRepository.activeRentalsByStore();
    }
}
