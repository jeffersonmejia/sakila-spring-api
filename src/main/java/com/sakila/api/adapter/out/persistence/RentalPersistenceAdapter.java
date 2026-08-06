package com.sakila.api.adapter.out.persistence;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sakila.api.adapter.out.persistence.entity.CustomerEntity;
import com.sakila.api.adapter.out.persistence.entity.InventoryEntity;
import com.sakila.api.adapter.out.persistence.entity.RentalEntity;
import com.sakila.api.adapter.out.persistence.entity.StaffEntity;
import com.sakila.api.adapter.out.persistence.mapper.EntityMapper;
import com.sakila.api.adapter.out.persistence.mapper.PageResultMapper;
import com.sakila.api.adapter.out.persistence.repository.JpaCustomerRepository;
import com.sakila.api.adapter.out.persistence.repository.JpaInventoryRepository;
import com.sakila.api.adapter.out.persistence.repository.JpaRentalRepository;
import com.sakila.api.adapter.out.persistence.repository.JpaStaffRepository;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.Rental;
import com.sakila.api.domain.model.SortDirection;
import com.sakila.api.domain.port.out.RentalRepository;

@Repository
@Transactional(readOnly = true)
public class RentalPersistenceAdapter implements RentalRepository {

    private static final List<String> SORTABLE = List.of("id", "rentalDate");

    private final JpaRentalRepository jpa;
    private final JpaInventoryRepository jpaInventory;
    private final JpaCustomerRepository jpaCustomer;
    private final JpaStaffRepository jpaStaff;

    public RentalPersistenceAdapter(JpaRentalRepository jpa, JpaInventoryRepository jpaInventory,
            JpaCustomerRepository jpaCustomer, JpaStaffRepository jpaStaff) {
        this.jpa = jpa;
        this.jpaInventory = jpaInventory;
        this.jpaCustomer = jpaCustomer;
        this.jpaStaff = jpaStaff;
    }

    @Override
    public Optional<Rental> findById(Integer id) {
        return jpa.findById(id).map(EntityMapper::toRental);
    }

    @Override
    public PageResult<Rental> findByCustomerId(Integer customerId, PageQuery query) {
        return map(jpa.findByCustomerId(customerId, pageable(query)));
    }

    @Override
    public PageResult<Rental> findActive(PageQuery query) {
        return map(jpa.findActive(pageable(query)));
    }

    @Override
    public PageResult<Rental> findOverdue(PageQuery query) {
        return map(jpa.findOverdue(pageable(query)));
    }

    @Override
    public boolean hasActiveRental(Integer inventoryId) {
        return jpa.existsByInventoryIdAndReturnDateIsNull(inventoryId);
    }

    @Override
    @Transactional
    public Rental save(Rental rental) {
        InventoryEntity inventory = jpaInventory.getReferenceById(rental.inventoryId());
        CustomerEntity customer = jpaCustomer.getReferenceById(rental.customerId());
        StaffEntity staff = jpaStaff.getReferenceById(rental.staffId());
        RentalEntity entity = new RentalEntity(null, Instant.now(), inventory, customer, staff);
        RentalEntity saved = jpa.save(entity);
        return EntityMapper.toRental(saved);
    }

    @Override
    @Transactional
    public Rental markReturned(Integer rentalId, Instant returnDate) {
        RentalEntity entity = jpa.findById(rentalId)
                .orElseThrow(() -> new com.sakila.api.common.exception.NotFoundException("Alquiler no encontrado"));
        entity.setReturnDate(returnDate);
        return EntityMapper.toRental(jpa.save(entity));
    }

    private PageResult<Rental> map(Page<RentalEntity> page) {
        return PageResultMapper.map(page, EntityMapper::toRental);
    }

    private Pageable pageable(PageQuery query) {
        String property = SORTABLE.contains(query.sortProperty()) ? query.sortProperty() : "id";
        Sort.Direction direction = query.direction() == SortDirection.DESC ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(query.page(), query.size(), Sort.by(direction, property));
    }
}
