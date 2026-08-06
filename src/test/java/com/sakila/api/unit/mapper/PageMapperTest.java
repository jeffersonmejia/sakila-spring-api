package com.sakila.api.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.sakila.api.adapter.in.web.dto.PageResponse;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.mapper.PageMapper;

class PageMapperTest {

    @Test
    void mapConvertsContentAndMetadata() {
        PageResult<String> page = new PageResult<>(List.of("a", "b"), 0, 2, 5, 3, true, false);

        PageResponse<String> response = PageMapper.map(page, String::toUpperCase);

        assertEquals(List.of("A", "B"), response.content());
        assertEquals(0, response.page());
        assertEquals(2, response.size());
        assertEquals(5, response.totalElements());
        assertEquals(3, response.totalPages());
        assertEquals(true, response.first());
        assertEquals(false, response.last());
    }

    @Test
    void mapWithEmptyContentReturnsEmptyList() {
        PageResult<String> page = new PageResult<>(List.of(), 2, 10, 0, 0, false, true);

        PageResponse<String> response = PageMapper.map(page, s -> s);

        assertEquals(List.of(), response.content());
    }
}
