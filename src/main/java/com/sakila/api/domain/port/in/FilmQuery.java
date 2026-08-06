package com.sakila.api.domain.port.in;

import java.util.List;

import com.sakila.api.domain.model.Actor;
import com.sakila.api.domain.model.Category;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;

public interface FilmQuery {

    PageResult<Film> listFilms(Integer categoryId, String rating, PageQuery query);

    PageResult<Film> searchByTitle(String title, PageQuery query);

    Film getFilm(Integer id);

    List<Actor> getFilmActors(Integer id);

    List<Category> getFilmCategories(Integer id);
}
