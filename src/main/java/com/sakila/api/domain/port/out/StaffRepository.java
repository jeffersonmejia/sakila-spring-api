package com.sakila.api.domain.port.out;

import java.util.Optional;

import com.sakila.api.domain.model.Staff;

public interface StaffRepository {

    Optional<Staff> findById(Integer id);
}
