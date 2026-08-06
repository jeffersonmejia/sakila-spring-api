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
import com.sakila.api.adapter.in.web.dto.FilmDetailResponse;
import com.sakila.api.adapter.in.web.dto.FilmResponse;
import com.sakila.api.adapter.in.web.dto.PageResponse;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.port.in.FilmQuery;
import com.sakila.api.mapper.FilmMapper;
import com.sakila.api.mapper.PageMapper;

@RestController
@RequestMapping("/api/films")
public class FilmController {

    private final FilmQuery filmQuery;

    public FilmController(FilmQuery filmQuery) {
        this.filmQuery = filmQuery;
    }

    @GetMapping
    public PageResponse<FilmResponse> listFilms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) String rating) {
        return PageMapper.map(
                filmQuery.listFilms(category, rating, PaginationSupport.of(page, size, sort)),
                FilmMapper::toResponse);
    }

    @GetMapping("/search")
    public PageResponse<FilmResponse> searchFilms(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(
                filmQuery.searchByTitle(title, PaginationSupport.of(page, size, sort)),
                FilmMapper::toResponse);
    }

    @GetMapping("/{id}")
    public FilmDetailResponse getFilm(@PathVariable Integer id) {
        Film film = filmQuery.getFilm(id);
        return FilmMapper.toDetail(film, filmQuery.getFilmActors(id), filmQuery.getFilmCategories(id));
    }

    @GetMapping("/{id}/actors")
    public List<ActorResponse> getActors(@PathVariable Integer id) {
        return filmQuery.getFilmActors(id).stream().map(FilmMapper::toActorResponse).toList();
    }

    @GetMapping("/{id}/categories")
    public List<CategoryResponse> getCategories(@PathVariable Integer id) {
        return filmQuery.getFilmCategories(id).stream().map(FilmMapper::toCategoryResponse).toList();
    }
}
