package com.sakila.api.adapter.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sakila.api.adapter.out.persistence.entity.CustomerEntity;

public interface JpaCustomerRepository extends JpaRepository<CustomerEntity, Integer> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<CustomerEntity> findByEmailIgnoreCase(String email);

    @Query("""
            select c from CustomerEntity c
            where lower(c.firstName) like lower(concat('%', :q, '%'))
               or lower(c.lastName) like lower(concat('%', :q, '%'))
               or (c.email is not null and lower(c.email) like lower(concat('%', :q, '%')))
            """)
    Page<CustomerEntity> search(@Param("q") String q, Pageable pageable);
}
