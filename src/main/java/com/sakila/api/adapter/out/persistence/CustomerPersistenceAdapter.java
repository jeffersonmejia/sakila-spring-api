package com.sakila.api.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sakila.api.adapter.out.persistence.entity.AddressEntity;
import com.sakila.api.adapter.out.persistence.entity.CustomerEntity;
import com.sakila.api.adapter.out.persistence.entity.StoreEntity;
import com.sakila.api.adapter.out.persistence.mapper.EntityMapper;
import com.sakila.api.adapter.out.persistence.mapper.PageResultMapper;
import com.sakila.api.adapter.out.persistence.repository.JpaAddressRepository;
import com.sakila.api.adapter.out.persistence.repository.JpaCustomerRepository;
import com.sakila.api.adapter.out.persistence.repository.JpaStoreRepository;
import com.sakila.api.domain.model.Customer;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.SortDirection;
import com.sakila.api.domain.port.out.CustomerRepository;

@Repository
@Transactional(readOnly = true)
public class CustomerPersistenceAdapter implements CustomerRepository {

    private static final List<String> SORTABLE = List.of("id", "firstName", "lastName", "email");

    private final JpaCustomerRepository jpa;
    private final JpaStoreRepository jpaStore;
    private final JpaAddressRepository jpaAddress;

    public CustomerPersistenceAdapter(JpaCustomerRepository jpa, JpaStoreRepository jpaStore,
            JpaAddressRepository jpaAddress) {
        this.jpa = jpa;
        this.jpaStore = jpaStore;
        this.jpaAddress = jpaAddress;
    }

    @Override
    public PageResult<Customer> findAll(PageQuery query) {
        return map(jpa.findAll(pageable(query)));
    }

    @Override
    public PageResult<Customer> search(String search, PageQuery query) {
        return map(jpa.search(search, pageable(query)));
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        return jpa.findById(id).map(EntityMapper::toCustomer);
    }

    @Override
    public Optional<Customer> findByEmailIgnoreCase(String email) {
        return jpa.findByEmailIgnoreCase(email).map(EntityMapper::toCustomer);
    }

    @Override
    public boolean existsByEmailIgnoreCase(String email) {
        return jpa.existsByEmailIgnoreCase(email);
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        CustomerEntity entity;
        if (customer.id() == null) {
            StoreEntity store = jpaStore.getReferenceById(customer.storeId());
            AddressEntity address = jpaAddress.getReferenceById(customer.address().id());
            entity = new CustomerEntity(null, customer.firstName(), customer.lastName(), customer.email(),
                    customer.active(), store, address);
        } else {
            entity = jpa.findById(customer.id())
                    .orElseThrow(() -> new com.sakila.api.common.exception.NotFoundException("Cliente no encontrado"));
            AddressEntity address = jpaAddress.getReferenceById(customer.address().id());
            entity.setFirstName(customer.firstName());
            entity.setLastName(customer.lastName());
            entity.setEmail(customer.email());
            entity.setActive(customer.active());
            entity.setAddress(address);
        }
        CustomerEntity saved = jpa.save(entity);
        jpa.flush();
        CustomerEntity reloaded = jpa.findById(saved.getId()).orElse(saved);
        return EntityMapper.toCustomer(reloaded);
    }

    private PageResult<Customer> map(Page<CustomerEntity> page) {
        return PageResultMapper.map(page, EntityMapper::toCustomer);
    }

    private Pageable pageable(PageQuery query) {
        String property = SORTABLE.contains(query.sortProperty()) ? query.sortProperty() : "id";
        Sort.Direction direction = query.direction() == SortDirection.DESC ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(query.page(), query.size(), Sort.by(direction, property));
    }
}
