package com.sakila.api.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sakila.api.common.exception.NotFoundException;
import com.sakila.api.domain.model.Actor;
import com.sakila.api.domain.model.Category;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.Language;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.SortDirection;
import com.sakila.api.domain.port.out.FilmRepository;
import com.sakila.api.domain.service.FilmService;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private FilmService filmService;

    private final PageQuery query = PageQuery.of(0, 20, "id", SortDirection.ASC);
    private final Film film = new Film(1, "ACADEMY", "desc", 2006, new Language(1, "English"),
            (short) 6, java.math.BigDecimal.ONE, (short) 86, java.math.BigDecimal.TEN, "PG");

    @Test
    void listFilmsWithoutFiltersUsesFindAll() {
        PageResult<Film> page = PageResult.of(List.of(film), 0, 20, 1);
        when(filmRepository.findAll(query)).thenReturn(page);

        PageResult<Film> result = filmService.listFilms(null, null, query);

        assertSame(page, result);
        verify(filmRepository, never()).findByFilters(org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void listFilmsWithCategoryUsesFilters() {
        PageResult<Film> page = PageResult.of(List.of(film), 0, 20, 1);
        when(filmRepository.findByFilters(1, null, query)).thenReturn(page);

        PageResult<Film> result = filmService.listFilms(1, null, query);

        assertSame(page, result);
        verify(filmRepository, never()).findAll(query);
    }

    @Test
    void listFilmsWithRatingUsesFilters() {
        PageResult<Film> page = PageResult.of(List.of(film), 0, 20, 1);
        when(filmRepository.findByFilters(null, "PG", query)).thenReturn(page);

        PageResult<Film> result = filmService.listFilms(null, "PG", query);

        assertSame(page, result);
    }

    @Test
    void searchByTitleDelegates() {
        PageResult<Film> page = PageResult.of(List.of(film), 0, 20, 1);
        when(filmRepository.searchByTitle("academy", query)).thenReturn(page);

        PageResult<Film> result = filmService.searchByTitle("academy", query);

        assertSame(page, result);
    }

    @Test
    void getFilmReturnsExisting() {
        when(filmRepository.findById(1)).thenReturn(Optional.of(film));

        Film result = filmService.getFilm(1);

        assertSame(film, result);
    }

    @Test
    void getFilmThrowsWhenMissing() {
        when(filmRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> filmService.getFilm(999));

        assertEquals("Película no encontrada", ex.getMessage());
    }

    @Test
    void getFilmActorsDelegates() {
        List<Actor> actors = List.of(new Actor(1, "PENELOPE", "GUINESS"));
        when(filmRepository.findById(1)).thenReturn(Optional.of(film));
        when(filmRepository.findActors(1)).thenReturn(actors);

        List<Actor> result = filmService.getFilmActors(1);

        assertSame(actors, result);
    }

    @Test
    void getFilmActorsThrowsWhenFilmMissing() {
        when(filmRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> filmService.getFilmActors(999));

        verify(filmRepository, never()).findActors(999);
    }

    @Test
    void getFilmCategoriesDelegates() {
        List<Category> categories = List.of(new Category(1, "Action"));
        when(filmRepository.findById(1)).thenReturn(Optional.of(film));
        when(filmRepository.findCategories(1)).thenReturn(categories);

        List<Category> result = filmService.getFilmCategories(1);

        assertSame(categories, result);
    }

    @Test
    void getFilmCategoriesThrowsWhenFilmMissing() {
        when(filmRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> filmService.getFilmCategories(999));

        verify(filmRepository, never()).findCategories(999);
    }
}
