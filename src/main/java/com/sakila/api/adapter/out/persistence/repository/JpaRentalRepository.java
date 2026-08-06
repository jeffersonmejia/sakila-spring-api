package com.sakila.api.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sakila.api.adapter.out.persistence.entity.RentalEntity;

public interface JpaRentalRepository extends JpaRepository<RentalEntity, Integer> {

    @Query("select r from RentalEntity r where r.customer.id = :customerId order by r.rentalDate desc")
    Page<RentalEntity> findByCustomerId(@Param("customerId") Integer customerId, Pageable pageable);

    @Query("select r from RentalEntity r where r.returnDate is null order by r.rentalDate desc")
    Page<RentalEntity> findActive(Pageable pageable);

    @Query("""
            select r from RentalEntity r
            where r.returnDate is null
              and r.rentalDate < (current_timestamp - (r.inventory.film.rentalDuration * 1 day))
            order by r.rentalDate desc
            """)
    Page<RentalEntity> findOverdue(Pageable pageable);

    boolean existsByInventoryIdAndReturnDateIsNull(Integer inventoryId);
}
