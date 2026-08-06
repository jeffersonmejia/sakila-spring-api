package com.sakila.api.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.sakila.api.adapter.out.persistence.mapper.PageResultMapper;
import com.sakila.api.domain.model.PageResult;

class PageResultMapperTest {

    @Test
    void mapConvertsSpringPageToDomainPage() {
        PageRequest pageable = PageRequest.of(1, 3);
        PageImpl<String> page = new PageImpl<>(List.of("a", "b", "c"), pageable, 10);

        PageResult<String> result = PageResultMapper.map(page, String::toUpperCase);

        assertEquals(List.of("A", "B", "C"), result.content());
        assertEquals(1, result.page());
        assertEquals(3, result.size());
        assertEquals(10, result.totalElements());
        assertEquals(4, result.totalPages());
        assertEquals(false, result.first());
        assertEquals(false, result.last());
    }

    @Test
    void mapWithEmptyPageMarksLast() {
        PageImpl<String> page = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

        PageResult<String> result = PageResultMapper.map(page, s -> s);

        assertEquals(0, result.totalPages());
        assertEquals(true, result.first());
        assertEquals(true, result.last());
    }
}
