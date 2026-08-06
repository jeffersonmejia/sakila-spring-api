package com.sakila.api.domain.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sakila.api.common.exception.ConflictException;
import com.sakila.api.common.exception.NotFoundException;
import com.sakila.api.domain.model.Customer;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.Rental;
import com.sakila.api.domain.port.in.RentalUseCases;
import com.sakila.api.domain.port.out.CustomerRepository;
import com.sakila.api.domain.port.out.InventoryRepository;
import com.sakila.api.domain.port.out.RentalRepository;
import com.sakila.api.domain.port.out.StaffRepository;

@Service
public class RentalService implements RentalUseCases {

    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final InventoryRepository inventoryRepository;
    private final StaffRepository staffRepository;

    public RentalService(RentalRepository rentalRepository, CustomerRepository customerRepository,
            InventoryRepository inventoryRepository, StaffRepository staffRepository) {
        this.rentalRepository = rentalRepository;
        this.customerRepository = customerRepository;
        this.inventoryRepository = inventoryRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    @Transactional
    public Rental createRental(Rental rental) {
        Customer customer = customerRepository.findById(rental.customerId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        if (!Boolean.TRUE.equals(customer.active())) {
            throw new ConflictException("El cliente no está activo");
        }
        inventoryRepository.findById(rental.inventoryId())
                .orElseThrow(() -> new NotFoundException("Copia no encontrada"));
        if (rentalRepository.hasActiveRental(rental.inventoryId())) {
            throw new ConflictException("La copia seleccionada no está disponible");
        }
        staffRepository.findById(rental.staffId())
                .orElseThrow(() -> new NotFoundException("Empleado no encontrado"));
        return rentalRepository.save(rental);
    }

    @Override
    public Rental getRental(Integer id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Alquiler no encontrado"));
    }

    @Override
    public PageResult<Rental> listActive(PageQuery query) {
        return rentalRepository.findActive(query);
    }

    @Override
    public PageResult<Rental> listOverdue(PageQuery query) {
        return rentalRepository.findOverdue(query);
    }

    @Override
    public PageResult<Rental> customerRentals(Integer customerId, PageQuery query) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        return rentalRepository.findByCustomerId(customerId, query);
    }

    @Override
    @Transactional
    public Rental returnRental(Integer id) {
        Rental rental = getRental(id);
        if (rental.returnDate() != null) {
            throw new ConflictException("El alquiler ya fue devuelto");
        }
        return rentalRepository.markReturned(id, Instant.now());
    }
}
