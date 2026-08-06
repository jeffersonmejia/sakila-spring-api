package com.sakila.api.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sakila.api.adapter.out.persistence.mapper.EntityMapper;
import com.sakila.api.adapter.out.persistence.repository.JpaStaffRepository;
import com.sakila.api.domain.model.Staff;
import com.sakila.api.domain.port.out.StaffRepository;

@Repository
@Transactional(readOnly = true)
public class StaffPersistenceAdapter implements StaffRepository {

    private final JpaStaffRepository jpa;

    public StaffPersistenceAdapter(JpaStaffRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Staff> findById(Integer id) {
        return jpa.findById(id).map(EntityMapper::toStaff);
    }
}
