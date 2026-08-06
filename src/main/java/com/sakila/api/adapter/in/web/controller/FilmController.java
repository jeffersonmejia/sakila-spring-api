package com.sakila.api.adapter.in.web.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sakila.api.adapter.in.web.PaginationSupport;
import com.sakila.api.adapter.in.web.dto.ActorResponse;
import com.sakila.api.adapter.in.web.dto.CategoryResponse;
import com.sakila.api.adapter.in.web.dto.ErrorResponse;
import com.sakila.api.adapter.in.web.dto.FilmDetailResponse;
import com.sakila.api.adapter.in.web.dto.FilmResponse;
import com.sakila.api.adapter.in.web.dto.PageResponse;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.port.in.FilmQuery;
import com.sakila.api.mapper.FilmMapper;
import com.sakila.api.mapper.PageMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/films")
@Tag(name = "Films", description = "Read operations over the film catalog. Any authenticated user can query them.")
public class FilmController {

    private final FilmQuery filmQuery;

    public FilmController(FilmQuery filmQuery) {
        this.filmQuery = filmQuery;
    }

    @GetMapping
    @Operation(summary = "List films",
            description = "Returns a paged list of films, optionally filtered by category and rating. "
                    + "The `sort` parameter accepts a property name and an optional direction, e.g. `title,desc`.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged list of films",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public PageResponse<FilmResponse> listFilms(
            @Parameter(description = "Zero based page index", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Maximum number of items per page", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort property and direction, format `property,direction`. Direction is `asc` or `desc`.",
                    example = "title,asc") @RequestParam(defaultValue = "id,asc") String sort,
            @Parameter(description = "Filter by category identifier", example = "1") @RequestParam(required = false) Integer category,
            @Parameter(description = "Filter by MPAA rating. Values `G`, `PG`, `PG-13`, `R`, `NC-17`.",
                    example = "PG") @RequestParam(required = false) String rating) {
        return PageMapper.map(
                filmQuery.listFilms(category, rating, PaginationSupport.of(page, size, sort)),
                FilmMapper::toResponse);
    }

    @GetMapping("/search")
    @Operation(summary = "Search films by title",
            description = "Returns a paged list of films whose title contains the given text, case insensitive.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged list of matching films",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Missing `title` query parameter",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public PageResponse<FilmResponse> searchFilms(
            @Parameter(description = "Title text to search for", example = "academy") @RequestParam String title,
            @Parameter(description = "Zero based page index", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Maximum number of items per page", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort property and direction, format `property,direction`.",
                    example = "id,asc") @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(
                filmQuery.searchByTitle(title, PaginationSupport.of(page, size, sort)),
                FilmMapper::toResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a film by identifier",
            description = "Returns the film detail including its summary, the actors in the cast and its categories. "
                    + "Responds with 404 when the film does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Film detail with cast and categories",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FilmDetailResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Film not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public FilmDetailResponse getFilm(
            @Parameter(description = "Identifier of the film", example = "1") @PathVariable Integer id) {
        Film film = filmQuery.getFilm(id);
        return FilmMapper.toDetail(film, filmQuery.getFilmActors(id), filmQuery.getFilmCategories(id));
    }

    @GetMapping("/{id}/actors")
    @Operation(summary = "List the actors of a film",
            description = "Returns the cast of the given film. Responds with 404 when the film does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of actors in the film",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ActorResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Film not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public List<ActorResponse> getActors(
            @Parameter(description = "Identifier of the film", example = "1") @PathVariable Integer id) {
        return filmQuery.getFilmActors(id).stream().map(FilmMapper::toActorResponse).toList();
    }

    @GetMapping("/{id}/categories")
    @Operation(summary = "List the categories of a film",
            description = "Returns the categories the given film belongs to. Responds with 404 when the film does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of categories of the film",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Film not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public List<CategoryResponse> getCategories(
            @Parameter(description = "Identifier of the film", example = "1") @PathVariable Integer id) {
        return filmQuery.getFilmCategories(id).stream().map(FilmMapper::toCategoryResponse).toList();
    }
}
