package com.sakila.api.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sakila.api.adapter.out.persistence.entity.AddressEntity;

public interface JpaAddressRepository extends JpaRepository<AddressEntity, Integer> {
}
