package com.sakila.api.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sakila.api.adapter.out.persistence.mapper.EntityMapper;
import com.sakila.api.adapter.out.persistence.repository.JpaAddressRepository;
import com.sakila.api.domain.model.Address;
import com.sakila.api.domain.port.out.AddressRepository;

@Repository
@Transactional(readOnly = true)
public class AddressPersistenceAdapter implements AddressRepository {

    private final JpaAddressRepository jpa;

    public AddressPersistenceAdapter(JpaAddressRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Address> findById(Integer id) {
        return jpa.findById(id).map(EntityMapper::toAddress);
    }
}
