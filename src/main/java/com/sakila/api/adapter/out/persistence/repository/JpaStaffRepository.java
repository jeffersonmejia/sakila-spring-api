package com.sakila.api.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sakila.api.adapter.out.persistence.entity.StaffEntity;

public interface JpaStaffRepository extends JpaRepository<StaffEntity, Integer> {
}
