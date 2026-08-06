package com.sakila.api.domain.port.out;

import java.util.Optional;

import com.sakila.api.domain.model.Store;

public interface StoreRepository {

    Optional<Store> findById(Integer id);
}
