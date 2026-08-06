package com.sakila.api.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sakila.api.adapter.out.persistence.entity.ActorEntity;
import com.sakila.api.adapter.out.persistence.entity.CategoryEntity;
import com.sakila.api.adapter.out.persistence.entity.FilmEntity;
import com.sakila.api.adapter.out.persistence.mapper.EntityMapper;
import com.sakila.api.adapter.out.persistence.mapper.PageResultMapper;
import com.sakila.api.adapter.out.persistence.repository.JpaFilmRepository;
import com.sakila.api.domain.model.Actor;
import com.sakila.api.domain.model.Category;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.SortDirection;
import com.sakila.api.domain.port.out.FilmRepository;

@Repository
@Transactional(readOnly = true)
public class FilmPersistenceAdapter implements FilmRepository {

    private static final List<String> SORTABLE = List.of("id", "title", "rating", "releaseYear");

    private final JpaFilmRepository jpa;

    public FilmPersistenceAdapter(JpaFilmRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Film> findById(Integer id) {
        return jpa.findById(id).map(EntityMapper::toFilm);
    }

    @Override
    public PageResult<Film> findAll(PageQuery query) {
        return map(jpa.findAll(pageable(query)));
    }

    @Override
    public PageResult<Film> searchByTitle(String title, PageQuery query) {
        return map(jpa.searchByTitle(title, pageable(query)));
    }

    @Override
    public PageResult<Film> findByFilters(Integer categoryId, String rating, PageQuery query) {
        return map(jpa.findByFilters(categoryId, rating, pageable(query)));
    }

    @Override
    public List<Actor> findActors(Integer filmId) {
        List<ActorEntity> entities = jpa.findActorsByFilmId(filmId);
        return entities.stream().map(EntityMapper::toActor).toList();
    }

    @Override
    public List<Category> findCategories(Integer filmId) {
        List<CategoryEntity> entities = jpa.findCategoriesByFilmId(filmId);
        return entities.stream().map(EntityMapper::toCategory).toList();
    }

    private PageResult<Film> map(Page<FilmEntity> page) {
        return PageResultMapper.map(page, EntityMapper::toFilm);
    }

    private Pageable pageable(PageQuery query) {
        String property = SORTABLE.contains(query.sortProperty()) ? query.sortProperty() : "id";
        Sort.Direction direction = query.direction() == SortDirection.DESC ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(query.page(), query.size(), Sort.by(direction, property));
    }
}
