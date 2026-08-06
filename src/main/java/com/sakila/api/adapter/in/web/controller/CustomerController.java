package com.sakila.api.adapter.in.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sakila.api.adapter.in.web.PaginationSupport;
import com.sakila.api.adapter.in.web.dto.CustomerRequest;
import com.sakila.api.adapter.in.web.dto.CustomerResponse;
import com.sakila.api.adapter.in.web.dto.CustomerStatusRequest;
import com.sakila.api.adapter.in.web.dto.CustomerUpdateRequest;
import com.sakila.api.adapter.in.web.dto.PageResponse;
import com.sakila.api.adapter.in.web.dto.RentalResponse;
import com.sakila.api.domain.model.Customer;
import com.sakila.api.domain.port.in.CustomerUseCases;
import com.sakila.api.mapper.CustomerMapper;
import com.sakila.api.mapper.PageMapper;
import com.sakila.api.mapper.RentalMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerUseCases customerUseCases;

    public CustomerController(CustomerUseCases customerUseCases) {
        this.customerUseCases = customerUseCases;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public CustomerResponse createCustomer(@Valid @RequestBody CustomerRequest request) {
        return CustomerMapper.toResponse(customerUseCases.createCustomer(CustomerMapper.toNewDomain(request)));
    }

    @GetMapping
    public PageResponse<CustomerResponse> listCustomers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        var query = PaginationSupport.of(page, size, sort);
        var result = search == null || search.isBlank()
                ? customerUseCases.listCustomers(query)
                : customerUseCases.searchCustomers(search, query);
        return PageMapper.map(result, CustomerMapper::toResponse);
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomer(@PathVariable Integer id) {
        return CustomerMapper.toResponse(customerUseCases.getCustomer(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public CustomerResponse updateCustomer(@PathVariable Integer id, @Valid @RequestBody CustomerUpdateRequest request) {
        Customer current = customerUseCases.getCustomer(id);
        return CustomerMapper.toResponse(
                customerUseCases.updateCustomer(id, CustomerMapper.toUpdateDomain(id, current, request)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public CustomerResponse changeCustomerStatus(@PathVariable Integer id, @Valid @RequestBody CustomerStatusRequest request) {
        return CustomerMapper.toResponse(customerUseCases.changeCustomerStatus(id, request.active()));
    }

    @GetMapping("/{id}/rentals")
    public PageResponse<RentalResponse> customerRentals(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(customerUseCases.getCustomerRentals(id, PaginationSupport.of(page, size, sort)),
                RentalMapper::toResponse);
    }
}
