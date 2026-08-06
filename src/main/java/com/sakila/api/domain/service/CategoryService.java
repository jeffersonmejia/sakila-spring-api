package com.sakila.api.domain.service;

import org.springframework.stereotype.Service;

import com.sakila.api.common.exception.NotFoundException;
import com.sakila.api.domain.model.Category;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.port.in.CategoryQuery;
import com.sakila.api.domain.port.out.CategoryRepository;
import com.sakila.api.domain.port.out.FilmRepository;

@Service
public class CategoryService implements CategoryQuery {

    private final CategoryRepository categoryRepository;
    private final FilmRepository filmRepository;

    public CategoryService(CategoryRepository categoryRepository, FilmRepository filmRepository) {
        this.categoryRepository = categoryRepository;
        this.filmRepository = filmRepository;
    }

    @Override
    public PageResult<Category> listCategories(PageQuery query) {
        return categoryRepository.findAll(query);
    }

    @Override
    public PageResult<Film> filmsByCategory(Integer categoryId, PageQuery query) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
        return filmRepository.findByFilters(categoryId, null, query);
    }
}
