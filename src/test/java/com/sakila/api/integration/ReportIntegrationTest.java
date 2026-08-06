package com.sakila.api.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

class ReportIntegrationTest extends AbstractIntegrationTest {

    @Test
    void mostRentedFilmsReturnsList() throws Exception {
        getJson("/api/reports/most-rented-films?limit=5", adminToken())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void topCustomersReturnsList() throws Exception {
        getJson("/api/reports/top-customers?limit=5", adminToken())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void revenueByCategoryReturnsList() throws Exception {
        getJson("/api/reports/revenue-by-category", adminToken())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void rentalsByMonthReturnsList() throws Exception {
        getJson("/api/reports/rentals-by-month", adminToken())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void activeRentalsByStoreReturnsList() throws Exception {
        getJson("/api/reports/active-rentals-by-store", adminToken())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
