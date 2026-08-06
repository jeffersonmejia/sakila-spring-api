package com.sakila.api.domain.port.out;

import java.util.Optional;

import com.sakila.api.domain.model.Address;

public interface AddressRepository {

    Optional<Address> findById(Integer id);
}
