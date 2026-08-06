package com.sakila.api.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sakila.api.adapter.out.persistence.entity.CategoryEntity;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Integer> {
}
