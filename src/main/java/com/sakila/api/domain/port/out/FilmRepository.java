package com.sakila.api.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.sakila.api.domain.model.Actor;
import com.sakila.api.domain.model.Category;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;

public interface FilmRepository {

    Optional<Film> findById(Integer id);

    PageResult<Film> findAll(PageQuery query);

    PageResult<Film> searchByTitle(String title, PageQuery query);

    PageResult<Film> findByFilters(Integer categoryId, String rating, PageQuery query);

    List<Actor> findActors(Integer filmId);

    List<Category> findCategories(Integer filmId);
}
