package com.sakila.api.unit.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sakila.api.domain.model.ActiveRentalsByStore;
import com.sakila.api.domain.model.MostRentedFilm;
import com.sakila.api.domain.model.RentalsByMonth;
import com.sakila.api.domain.model.RevenueByCategory;
import com.sakila.api.domain.model.TopCustomer;
import com.sakila.api.domain.port.out.ReportRepository;
import com.sakila.api.domain.service.ReportService;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void mostRentedFilmsDelegates() {
        List<MostRentedFilm> expected = List.of(new MostRentedFilm(1, "ACADEMY", 42L));
        when(reportRepository.mostRentedFilms(5)).thenReturn(expected);

        List<MostRentedFilm> result = reportService.mostRentedFilms(5);

        assertSame(expected, result);
    }

    @Test
    void topCustomersDelegates() {
        List<TopCustomer> expected = List.of(new TopCustomer(1, "MARY SMITH", 9L));
        when(reportRepository.topCustomers(5)).thenReturn(expected);

        List<TopCustomer> result = reportService.topCustomers(5);

        assertSame(expected, result);
    }

    @Test
    void revenueByCategoryDelegates() {
        List<RevenueByCategory> expected = List.of(
                new RevenueByCategory("Action", new java.math.BigDecimal("99.99")));
        when(reportRepository.revenueByCategory()).thenReturn(expected);

        List<RevenueByCategory> result = reportService.revenueByCategory();

        assertSame(expected, result);
    }

    @Test
    void rentalsByMonthDelegates() {
        List<RentalsByMonth> expected = List.of(new RentalsByMonth("2026-01", 12L));
        when(reportRepository.rentalsByMonth()).thenReturn(expected);

        List<RentalsByMonth> result = reportService.rentalsByMonth();

        assertSame(expected, result);
    }

    @Test
    void activeRentalsByStoreDelegates() {
        List<ActiveRentalsByStore> expected = List.of(new ActiveRentalsByStore(1, 5L));
        when(reportRepository.activeRentalsByStore()).thenReturn(expected);

        List<ActiveRentalsByStore> result = reportService.activeRentalsByStore();

        assertSame(expected, result);
    }
}
