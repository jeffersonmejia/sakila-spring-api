package com.sakila.api.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sakila.api.adapter.out.persistence.mapper.EntityMapper;
import com.sakila.api.adapter.out.persistence.repository.JpaStoreRepository;
import com.sakila.api.domain.model.Store;
import com.sakila.api.domain.port.out.StoreRepository;

@Repository
@Transactional(readOnly = true)
public class StorePersistenceAdapter implements StoreRepository {

    private final JpaStoreRepository jpa;

    public StorePersistenceAdapter(JpaStoreRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Store> findById(Integer id) {
        return jpa.findById(id).map(EntityMapper::toStore);
    }
}
