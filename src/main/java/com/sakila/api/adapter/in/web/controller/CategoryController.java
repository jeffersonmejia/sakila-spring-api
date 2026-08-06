package com.sakila.api.adapter.in.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sakila.api.adapter.in.web.PaginationSupport;
import com.sakila.api.adapter.in.web.dto.CategoryResponse;
import com.sakila.api.adapter.in.web.dto.FilmResponse;
import com.sakila.api.adapter.in.web.dto.PageResponse;
import com.sakila.api.domain.port.in.CategoryQuery;
import com.sakila.api.mapper.FilmMapper;
import com.sakila.api.mapper.PageMapper;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryQuery categoryQuery;

    public CategoryController(CategoryQuery categoryQuery) {
        this.categoryQuery = categoryQuery;
    }

    @GetMapping
    public PageResponse<CategoryResponse> listCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(categoryQuery.listCategories(PaginationSupport.of(page, size, sort)),
                c -> new CategoryResponse(c.id(), c.name()));
    }

    @GetMapping("/{id}/films")
    public PageResponse<FilmResponse> filmsByCategory(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(categoryQuery.filmsByCategory(id, PaginationSupport.of(page, size, sort)),
                FilmMapper::toResponse);
    }
}
