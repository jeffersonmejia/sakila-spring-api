package com.sakila.api.adapter.in.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sakila.api.adapter.in.web.PaginationSupport;
import com.sakila.api.adapter.in.web.dto.PageResponse;
import com.sakila.api.adapter.in.web.dto.RentalRequest;
import com.sakila.api.adapter.in.web.dto.RentalResponse;
import com.sakila.api.domain.model.Rental;
import com.sakila.api.domain.port.in.RentalUseCases;
import com.sakila.api.mapper.PageMapper;
import com.sakila.api.mapper.RentalMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalUseCases rentalUseCases;

    public RentalController(RentalUseCases rentalUseCases) {
        this.rentalUseCases = rentalUseCases;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public RentalResponse createRental(@Valid @RequestBody RentalRequest request) {
        Rental rental = new Rental(null, null, null, request.customerId(), request.inventoryId(), null, request.staffId());
        return RentalMapper.toResponse(rentalUseCases.createRental(rental));
    }

    @GetMapping("/active")
    public PageResponse<RentalResponse> activeRentals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(rentalUseCases.listActive(PaginationSupport.of(page, size, sort)), RentalMapper::toResponse);
    }

    @GetMapping("/overdue")
    public PageResponse<RentalResponse> overdueRentals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(rentalUseCases.listOverdue(PaginationSupport.of(page, size, sort)), RentalMapper::toResponse);
    }

    @GetMapping("/{id}")
    public RentalResponse getRental(@PathVariable Integer id) {
        return RentalMapper.toResponse(rentalUseCases.getRental(id));
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public RentalResponse returnRental(@PathVariable Integer id) {
        return RentalMapper.toResponse(rentalUseCases.returnRental(id));
    }
}
