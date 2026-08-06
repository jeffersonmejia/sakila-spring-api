package com.sakila.api.domain.port.out;

import java.util.Optional;

import com.sakila.api.domain.model.Customer;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;

public interface CustomerRepository {

    PageResult<Customer> findAll(PageQuery query);

    PageResult<Customer> search(String search, PageQuery query);

    Optional<Customer> findById(Integer id);

    Optional<Customer> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    Customer save(Customer customer);
}
