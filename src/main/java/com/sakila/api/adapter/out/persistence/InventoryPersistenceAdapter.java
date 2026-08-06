package com.sakila.api.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sakila.api.adapter.out.persistence.entity.InventoryEntity;
import com.sakila.api.adapter.out.persistence.mapper.EntityMapper;
import com.sakila.api.adapter.out.persistence.mapper.PageResultMapper;
import com.sakila.api.adapter.out.persistence.repository.JpaInventoryRepository;
import com.sakila.api.adapter.out.persistence.repository.JpaRentalRepository;
import com.sakila.api.domain.model.Inventory;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.SortDirection;
import com.sakila.api.domain.port.out.InventoryRepository;

@Repository
@Transactional(readOnly = true)
public class InventoryPersistenceAdapter implements InventoryRepository {

    private static final List<String> SORTABLE = List.of("id");

    private final JpaInventoryRepository jpa;
    private final JpaRentalRepository jpaRental;

    public InventoryPersistenceAdapter(JpaInventoryRepository jpa, JpaRentalRepository jpaRental) {
        this.jpa = jpa;
        this.jpaRental = jpaRental;
    }

    @Override
    public PageResult<Inventory> findAll(PageQuery query) {
        Page<InventoryEntity> page = jpa.findAll(pageable(query));
        return PageResultMapper.map(page, this::toInventory);
    }

    @Override
    public PageResult<Inventory> findByFilmId(Integer filmId, PageQuery query) {
        Page<InventoryEntity> page = jpa.findByFilmId(filmId, pageable(query));
        return PageResultMapper.map(page, this::toInventory);
    }

    @Override
    public Optional<Inventory> findById(Integer id) {
        return jpa.findById(id).map(e -> EntityMapper.toInventory(e, !hasActiveRental(id)));
    }

    @Override
    public List<Inventory> findAvailableByFilmId(Integer filmId) {
        return jpa.findAvailableByFilmId(filmId).stream().map(e -> EntityMapper.toInventory(e, true)).toList();
    }

    @Override
    public long countByFilmId(Integer filmId) {
        return jpa.countByFilmId(filmId);
    }

    @Override
    public boolean hasActiveRental(Integer inventoryId) {
        return jpaRental.existsByInventoryIdAndReturnDateIsNull(inventoryId);
    }

    private Inventory toInventory(InventoryEntity e) {
        return EntityMapper.toInventory(e, !jpaRental.existsByInventoryIdAndReturnDateIsNull(e.getId()));
    }

    private Pageable pageable(PageQuery query) {
        String property = SORTABLE.contains(query.sortProperty()) ? query.sortProperty() : "id";
        Sort.Direction direction = query.direction() == SortDirection.DESC ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(query.page(), query.size(), Sort.by(direction, property));
    }
}
