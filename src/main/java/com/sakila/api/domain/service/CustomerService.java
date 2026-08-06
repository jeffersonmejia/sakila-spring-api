package com.sakila.api.domain.service;

import org.springframework.stereotype.Service;

import com.sakila.api.common.exception.ConflictException;
import com.sakila.api.common.exception.NotFoundException;
import com.sakila.api.domain.model.Customer;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.Rental;
import com.sakila.api.domain.port.in.CustomerUseCases;
import com.sakila.api.domain.port.out.AddressRepository;
import com.sakila.api.domain.port.out.CustomerRepository;
import com.sakila.api.domain.port.out.RentalRepository;
import com.sakila.api.domain.port.out.StoreRepository;

@Service
public class CustomerService implements CustomerUseCases {

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final AddressRepository addressRepository;
    private final RentalRepository rentalRepository;

    public CustomerService(CustomerRepository customerRepository, StoreRepository storeRepository,
            AddressRepository addressRepository, RentalRepository rentalRepository) {
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
        this.addressRepository = addressRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public PageResult<Customer> listCustomers(PageQuery query) {
        return customerRepository.findAll(query);
    }

    @Override
    public PageResult<Customer> searchCustomers(String search, PageQuery query) {
        return customerRepository.search(search, query);
    }

    @Override
    public Customer getCustomer(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
    }

    @Override
    public Customer createCustomer(Customer customer) {
        storeRepository.findById(customer.storeId())
                .orElseThrow(() -> new NotFoundException("Tienda no encontrada"));
        addressRepository.findById(customer.address().id())
                .orElseThrow(() -> new NotFoundException("Dirección no encontrada"));
        if (customerRepository.existsByEmailIgnoreCase(customer.email())) {
            throw new ConflictException("El correo electrónico ya está registrado");
        }
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Integer id, Customer customer) {
        getCustomer(id);
        customerRepository.findByEmailIgnoreCase(customer.email())
                .filter(other -> !other.id().equals(id))
                .ifPresent(other -> {
                    throw new ConflictException("El correo electrónico ya está registrado");
                });
        addressRepository.findById(customer.address().id())
                .orElseThrow(() -> new NotFoundException("Dirección no encontrada"));
        return customerRepository.save(customer);
    }

    @Override
    public Customer changeCustomerStatus(Integer id, Boolean active) {
        Customer current = getCustomer(id);
        Customer updated = new Customer(current.id(), current.firstName(), current.lastName(), current.email(),
                active, current.storeId(), current.address(), current.createDate());
        return customerRepository.save(updated);
    }

    @Override
    public PageResult<Rental> getCustomerRentals(Integer id, PageQuery query) {
        getCustomer(id);
        return rentalRepository.findByCustomerId(id, query);
    }
}
