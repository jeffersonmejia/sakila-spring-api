package com.sakila.api.unit.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.sakila.api.domain.model.PageResult;

class PageResultTest {

    @Test
    void ofComputesTotalPages() {
        PageResult<String> page = PageResult.of(List.of("a"), 0, 10, 35);

        assertEquals(35, page.totalElements());
        assertEquals(4, page.totalPages());
        assertTrue(page.first());
        assertFalse(page.last());
    }

    @Test
    void ofExactMultipleComputesPages() {
        PageResult<String> page = PageResult.of(List.of(), 1, 10, 20);

        assertEquals(2, page.totalPages());
        assertFalse(page.first());
        assertTrue(page.last());
    }

    @Test
    void ofEmptyContentMarksFirstAndLast() {
        PageResult<String> page = PageResult.of(List.of(), 0, 10, 0);

        assertEquals(0, page.totalPages());
        assertTrue(page.first());
        assertTrue(page.last());
    }

    @Test
    void ofLastPageDetection() {
        PageResult<String> page = PageResult.of(List.of(), 3, 10, 30);

        assertEquals(3, page.totalPages());
        assertTrue(page.last());
    }
}
