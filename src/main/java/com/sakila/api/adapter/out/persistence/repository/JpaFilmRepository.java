package com.sakila.api.adapter.out.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sakila.api.adapter.out.persistence.entity.ActorEntity;
import com.sakila.api.adapter.out.persistence.entity.CategoryEntity;
import com.sakila.api.adapter.out.persistence.entity.FilmEntity;

public interface JpaFilmRepository extends JpaRepository<FilmEntity, Integer> {

    @Query("select f from FilmEntity f where lower(f.title) like lower(concat('%', :title, '%'))")
    Page<FilmEntity> searchByTitle(@Param("title") String title, Pageable pageable);

    @Query("""
            select f from FilmEntity f
            where (:categoryId is null or exists (select 1 from f.categories c where c.id = :categoryId))
              and (:rating is null or f.rating = :rating)
            """)
    Page<FilmEntity> findByFilters(@Param("categoryId") Integer categoryId, @Param("rating") String rating,
            Pageable pageable);

    @Query("select a from FilmEntity f join f.actors a where f.id = :filmId order by a.firstName, a.lastName")
    List<ActorEntity> findActorsByFilmId(@Param("filmId") Integer filmId);

    @Query("select c from FilmEntity f join f.categories c where f.id = :filmId order by c.name")
    List<CategoryEntity> findCategoriesByFilmId(@Param("filmId") Integer filmId);
}
