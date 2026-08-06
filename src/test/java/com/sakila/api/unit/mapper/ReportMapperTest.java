package com.sakila.api.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

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
import com.sakila.api.mapper.ReportMapper;

class ReportMapperTest {

    @Test
    void toMostRentedMapsFields() {
        MostRentedFilmResponse response = ReportMapper.toMostRented(new MostRentedFilm(3, "JERICHO MULAN", 42L));

        assertEquals(3, response.filmId());
        assertEquals("JERICHO MULAN", response.title());
        assertEquals(42L, response.timesRented());
    }

    @Test
    void toTopCustomerMapsFields() {
        TopCustomerResponse response = ReportMapper.toTopCustomer(new TopCustomer(1, "MARY SMITH", 15L));

        assertEquals(1, response.customerId());
        assertEquals("MARY SMITH", response.name());
        assertEquals(15L, response.rentalsCount());
    }

    @Test
    void toRevenueMapsFields() {
        RevenueByCategoryResponse response = ReportMapper.toRevenue(new RevenueByCategory("Action", new BigDecimal("99.95")));

        assertEquals("Action", response.category());
        assertEquals(new BigDecimal("99.95"), response.revenue());
    }

    @Test
    void toRentalsByMonthMapsFields() {
        RentalsByMonthResponse response = ReportMapper.toRentalsByMonth(new RentalsByMonth("2026-07", 31L));

        assertEquals("2026-07", response.month());
        assertEquals(31L, response.rentals());
    }

    @Test
    void toActiveByStoreMapsFields() {
        ActiveRentalsByStoreResponse response = ReportMapper.toActiveByStore(new ActiveRentalsByStore(1, 25L));

        assertEquals(1, response.storeId());
        assertEquals(25L, response.activeCount());
    }
}
