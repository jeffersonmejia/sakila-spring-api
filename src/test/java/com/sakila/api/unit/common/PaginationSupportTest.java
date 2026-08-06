package com.sakila.api.unit.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.sakila.api.adapter.in.web.PaginationSupport;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.SortDirection;

class PaginationSupportTest {

    @Test
    void ofWithNullSortUsesIdAsc() {
        PageQuery query = PaginationSupport.of(2, 20, null);

        assertEquals(2, query.page());
        assertEquals(20, query.size());
        assertEquals("id", query.sortProperty());
        assertEquals(SortDirection.ASC, query.direction());
    }

    @Test
    void ofWithBlankSortUsesDefaults() {
        PageQuery query = PaginationSupport.of(0, 10, "  ");

        assertEquals("id", query.sortProperty());
        assertEquals(SortDirection.ASC, query.direction());
    }

    @Test
    void ofWithPropertyOnlyUsesAsc() {
        PageQuery query = PaginationSupport.of(0, 10, "title");

        assertEquals("title", query.sortProperty());
        assertEquals(SortDirection.ASC, query.direction());
    }

    @Test
    void ofWithDescDirectionParses() {
        PageQuery query = PaginationSupport.of(0, 10, "rentalDate,desc");

        assertEquals("rentalDate", query.sortProperty());
        assertEquals(SortDirection.DESC, query.direction());
    }

    @Test
    void ofWithUppercaseDescParses() {
        PageQuery query = PaginationSupport.of(0, 10, "id,DESC");

        assertEquals(SortDirection.DESC, query.direction());
    }

    @Test
    void ofWithUnknownDirectionFallsBackToAsc() {
        PageQuery query = PaginationSupport.of(0, 10, "title,sideways");

        assertEquals(SortDirection.ASC, query.direction());
    }

    @Test
    void ofWithExtraTokensUsesFirstTwo() {
        PageQuery query = PaginationSupport.of(0, 10, "title,asc,extra");

        assertEquals("title", query.sortProperty());
        assertEquals(SortDirection.ASC, query.direction());
    }
}
