package com.sakila.api.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.sakila.api.domain.model.Inventory;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;

public interface InventoryRepository {

    PageResult<Inventory> findAll(PageQuery query);

    PageResult<Inventory> findByFilmId(Integer filmId, PageQuery query);

    Optional<Inventory> findById(Integer id);

    List<Inventory> findAvailableByFilmId(Integer filmId);

    long countByFilmId(Integer filmId);

    boolean hasActiveRental(Integer inventoryId);
}
