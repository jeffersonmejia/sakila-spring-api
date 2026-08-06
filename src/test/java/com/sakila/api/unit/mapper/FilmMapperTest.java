package com.sakila.api.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.sakila.api.adapter.in.web.dto.ActorResponse;
import com.sakila.api.adapter.in.web.dto.CategoryResponse;
import com.sakila.api.adapter.in.web.dto.FilmDetailResponse;
import com.sakila.api.adapter.in.web.dto.FilmResponse;
import com.sakila.api.adapter.in.web.dto.LanguageResponse;
import com.sakila.api.domain.model.Actor;
import com.sakila.api.domain.model.Category;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.Language;
import com.sakila.api.mapper.FilmMapper;

class FilmMapperTest {

    private final Language language = new Language(1, "English");
    private final Film film = new Film(1, "ACADEMY", "desc", 2006, language, (short) 6, BigDecimal.ONE, (short) 86,
            BigDecimal.TEN, "PG");

    @Test
    void toResponseMapsAllFields() {
        FilmResponse response = FilmMapper.toResponse(film);

        assertEquals(1, response.id());
        assertEquals("ACADEMY", response.title());
        assertEquals("desc", response.description());
        assertEquals(2006, response.releaseYear());
        assertEquals("English", response.language());
        assertEquals((short) 6, response.rentalDuration());
        assertEquals(BigDecimal.ONE, response.rentalRate());
        assertEquals((short) 86, response.length());
        assertEquals(BigDecimal.TEN, response.replacementCost());
        assertEquals("PG", response.rating());
    }

    @Test
    void toResponseWithNullLanguageMapsNull() {
        Film withoutLanguage = new Film(1, "ACADEMY", "desc", 2006, null, (short) 6, BigDecimal.ONE, (short) 86,
                BigDecimal.TEN, "PG");

        FilmResponse response = FilmMapper.toResponse(withoutLanguage);

        assertNull(response.language());
    }

    @Test
    void toDetailMapsCastAndCategories() {
        List<Actor> actors = List.of(new Actor(1, "PENELOPE", "GUINESS"));
        List<Category> categories = List.of(new Category(1, "Action"));

        FilmDetailResponse response = FilmMapper.toDetail(film, actors, categories);

        assertEquals("ACADEMY", response.film().title());
        assertEquals(1, response.actors().size());
        assertEquals("PENELOPE", response.actors().getFirst().firstName());
        assertEquals(1, response.categories().size());
        assertEquals("Action", response.categories().getFirst().name());
    }

    @Test
    void toActorResponseMapsFields() {
        ActorResponse response = FilmMapper.toActorResponse(new Actor(2, "NICK", "WAHLBERG"));

        assertEquals(2, response.id());
        assertEquals("NICK", response.firstName());
        assertEquals("WAHLBERG", response.lastName());
    }

    @Test
    void toCategoryResponseMapsFields() {
        CategoryResponse response = FilmMapper.toCategoryResponse(new Category(3, "Comedy"));

        assertEquals(3, response.id());
        assertEquals("Comedy", response.name());
    }

    @Test
    void toLanguageResponseMapsFields() {
        LanguageResponse response = FilmMapper.toLanguageResponse(new Language(1, "English"));

        assertEquals(1, response.id());
        assertEquals("English", response.name());
    }
}
