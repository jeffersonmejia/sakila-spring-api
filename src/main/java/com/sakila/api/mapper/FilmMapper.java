package com.sakila.api.mapper;

import java.util.List;

import com.sakila.api.adapter.in.web.dto.ActorResponse;
import com.sakila.api.adapter.in.web.dto.CategoryResponse;
import com.sakila.api.adapter.in.web.dto.FilmDetailResponse;
import com.sakila.api.adapter.in.web.dto.FilmResponse;
import com.sakila.api.adapter.in.web.dto.LanguageResponse;
import com.sakila.api.domain.model.Actor;
import com.sakila.api.domain.model.Category;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.Language;

public final class FilmMapper {

    private FilmMapper() {
    }

    public static FilmResponse toResponse(Film film) {
        return new FilmResponse(film.id(), film.title(), film.description(), film.releaseYear(),
                film.language() != null ? film.language().name() : null, film.rentalDuration(), film.rentalRate(),
                film.length(), film.replacementCost(), film.rating());
    }

    public static FilmDetailResponse toDetail(Film film, List<Actor> actors, List<Category> categories) {
        return new FilmDetailResponse(toResponse(film),
                actors.stream().map(FilmMapper::toActorResponse).toList(),
                categories.stream().map(FilmMapper::toCategoryResponse).toList());
    }

    public static ActorResponse toActorResponse(Actor actor) {
        return new ActorResponse(actor.id(), actor.firstName(), actor.lastName());
    }

    public static CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(category.id(), category.name());
    }

    public static LanguageResponse toLanguageResponse(Language language) {
        return new LanguageResponse(language.id(), language.name());
    }
}
