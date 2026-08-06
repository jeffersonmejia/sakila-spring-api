package com.sakila.api.adapter.out.persistence;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sakila.api.adapter.out.persistence.repository.JpaReportRepository;
import com.sakila.api.domain.model.ActiveRentalsByStore;
import com.sakila.api.domain.model.MostRentedFilm;
import com.sakila.api.domain.model.RentalsByMonth;
import com.sakila.api.domain.model.RevenueByCategory;
import com.sakila.api.domain.model.TopCustomer;
import com.sakila.api.domain.port.out.ReportRepository;

@Repository
@Transactional(readOnly = true)
public class ReportPersistenceAdapter implements ReportRepository {

    private final JpaReportRepository jpa;

    public ReportPersistenceAdapter(JpaReportRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<MostRentedFilm> mostRentedFilms(int limit) {
        return jpa.mostRentedFilms(PageRequest.of(0, limit));
    }

    @Override
    public List<TopCustomer> topCustomers(int limit) {
        return jpa.topCustomers(PageRequest.of(0, limit));
    }

    @Override
    public List<RevenueByCategory> revenueByCategory() {
        return jpa.revenueByCategory();
    }

    @Override
    public List<RentalsByMonth> rentalsByMonth() {
        return jpa.rentalsByMonth();
    }

    @Override
    public List<ActiveRentalsByStore> activeRentalsByStore() {
        return jpa.activeRentalsByStore();
    }
}
