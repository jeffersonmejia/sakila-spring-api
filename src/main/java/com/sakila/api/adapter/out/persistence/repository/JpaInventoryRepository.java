package com.sakila.api.adapter.out.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sakila.api.adapter.out.persistence.entity.InventoryEntity;

public interface JpaInventoryRepository extends JpaRepository<InventoryEntity, Integer> {

    @Query("select i from InventoryEntity i where i.film.id = :filmId")
    Page<InventoryEntity> findByFilmId(@Param("filmId") Integer filmId, Pageable pageable);

    @Query("select count(i) from InventoryEntity i where i.film.id = :filmId")
    long countByFilmId(@Param("filmId") Integer filmId);

    @Query("""
            select i from InventoryEntity i
            where i.film.id = :filmId
              and not exists (select r from RentalEntity r where r.inventory = i and r.returnDate is null)
            order by i.id
            """)
    List<InventoryEntity> findAvailableByFilmId(@Param("filmId") Integer filmId);
}
