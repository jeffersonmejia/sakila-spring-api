package com.sakila.api.adapter.in.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sakila.api.adapter.in.web.PaginationSupport;
import com.sakila.api.adapter.in.web.dto.CategoryResponse;
import com.sakila.api.adapter.in.web.dto.ErrorResponse;
import com.sakila.api.adapter.in.web.dto.FilmResponse;
import com.sakila.api.adapter.in.web.dto.PageResponse;
import com.sakila.api.domain.port.in.CategoryQuery;
import com.sakila.api.mapper.FilmMapper;
import com.sakila.api.mapper.PageMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Read operations over film categories. Any authenticated user can query them.")
public class CategoryController {

    private final CategoryQuery categoryQuery;

    public CategoryController(CategoryQuery categoryQuery) {
        this.categoryQuery = categoryQuery;
    }

    @GetMapping
    @Operation(summary = "List categories",
            description = "Returns a paged list of all film categories.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged list of categories",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public PageResponse<CategoryResponse> listCategories(
            @Parameter(description = "Zero based page index", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Maximum number of items per page", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort property and direction, format `property,direction`.",
                    example = "name,asc") @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(categoryQuery.listCategories(PaginationSupport.of(page, size, sort)),
                c -> new CategoryResponse(c.id(), c.name()));
    }

    @GetMapping("/{id}/films")
    @Operation(summary = "List the films of a category",
            description = "Returns a paged list of films that belong to the given category. "
                    + "Responds with 404 when the category does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged list of films in the category",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public PageResponse<FilmResponse> filmsByCategory(
            @Parameter(description = "Identifier of the category", example = "1") @PathVariable Integer id,
            @Parameter(description = "Zero based page index", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Maximum number of items per page", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort property and direction, format `property,direction`.",
                    example = "title,asc") @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(categoryQuery.filmsByCategory(id, PaginationSupport.of(page, size, sort)),
                FilmMapper::toResponse);
    }
}
