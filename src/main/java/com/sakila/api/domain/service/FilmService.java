package com.sakila.api.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sakila.api.common.exception.NotFoundException;
import com.sakila.api.domain.model.Actor;
import com.sakila.api.domain.model.Category;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.port.in.FilmQuery;
import com.sakila.api.domain.port.out.FilmRepository;

@Service
public class FilmService implements FilmQuery {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public PageResult<Film> listFilms(Integer categoryId, String rating, PageQuery query) {
        if (categoryId == null && rating == null) {
            return filmRepository.findAll(query);
        }
        return filmRepository.findByFilters(categoryId, rating, query);
    }

    @Override
    public PageResult<Film> searchByTitle(String title, PageQuery query) {
        return filmRepository.searchByTitle(title, query);
    }

    @Override
    public Film getFilm(Integer id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Película no encontrada"));
    }

    @Override
    public List<Actor> getFilmActors(Integer id) {
        getFilm(id);
        return filmRepository.findActors(id);
    }

    @Override
    public List<Category> getFilmCategories(Integer id) {
        getFilm(id);
        return filmRepository.findCategories(id);
    }
}
