package com.sakila.api.unit.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.SortDirection;

class PageQueryTest {

    @Test
    void ofClampsNegativePageToZero() {
        PageQuery query = PageQuery.of(-5, 10, "id", SortDirection.ASC);

        assertEquals(0, query.page());
    }

    @Test
    void ofClampsSizeBelowOneToOne() {
        PageQuery query = PageQuery.of(0, 0, "id", SortDirection.ASC);

        assertEquals(1, query.size());
    }

    @Test
    void ofKeepsValidValues() {
        PageQuery query = PageQuery.of(3, 25, "title", SortDirection.DESC);

        assertEquals(3, query.page());
        assertEquals(25, query.size());
        assertEquals("title", query.sortProperty());
        assertEquals(SortDirection.DESC, query.direction());
    }
}
