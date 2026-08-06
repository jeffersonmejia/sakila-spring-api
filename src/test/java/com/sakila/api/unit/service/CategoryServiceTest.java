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
import com.sakila.api.domain.model.Category;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.Language;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.SortDirection;
import com.sakila.api.domain.port.out.CategoryRepository;
import com.sakila.api.domain.port.out.FilmRepository;
import com.sakila.api.domain.service.CategoryService;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private CategoryService categoryService;

    private final PageQuery query = PageQuery.of(0, 20, "id", SortDirection.ASC);
    private final Category category = new Category(1, "Action");

    @Test
    void listCategoriesDelegates() {
        PageResult<Category> page = PageResult.of(List.of(category), 0, 20, 1);
        when(categoryRepository.findAll(query)).thenReturn(page);

        PageResult<Category> result = categoryService.listCategories(query);

        assertSame(page, result);
    }

    @Test
    void filmsByCategoryDelegatesWhenCategoryExists() {
        Film film = new Film(1, "ACADEMY", "desc", 2006, new Language(1, "English"),
                (short) 6, java.math.BigDecimal.ONE, (short) 86, java.math.BigDecimal.TEN, "PG");
        PageResult<Film> page = PageResult.of(List.of(film), 0, 20, 1);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(filmRepository.findByFilters(1, null, query)).thenReturn(page);

        PageResult<Film> result = categoryService.filmsByCategory(1, query);

        assertSame(page, result);
    }

    @Test
    void filmsByCategoryThrowsWhenCategoryMissing() {
        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> categoryService.filmsByCategory(999, query));

        assertEquals("Categoría no encontrada", ex.getMessage());
        verify(filmRepository, never()).findByFilters(org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }
}
