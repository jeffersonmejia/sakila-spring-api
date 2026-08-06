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
import com.sakila.api.adapter.in.web.dto.ErrorResponse;
import com.sakila.api.adapter.in.web.dto.PageResponse;
import com.sakila.api.adapter.in.web.dto.RentalResponse;
import com.sakila.api.domain.model.Customer;
import com.sakila.api.domain.port.in.CustomerUseCases;
import com.sakila.api.mapper.CustomerMapper;
import com.sakila.api.mapper.PageMapper;
import com.sakila.api.mapper.RentalMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Customer management. Writes require the ADMIN or EMPLOYEE role; reads are available to any authenticated user.")
public class CustomerController {

    private final CustomerUseCases customerUseCases;

    public CustomerController(CustomerUseCases customerUseCases) {
        this.customerUseCases = customerUseCases;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Register a new customer",
            description = "Creates a customer with the given data. The email must be unique and the store and address "
                    + "must exist. Responds with 409 when the email is already registered and 404 when the store or "
                    + "address does not exist. Requires the ADMIN or EMPLOYEE role.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without the required role",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Store or address not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already registered",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public CustomerResponse createCustomer(@Valid @RequestBody CustomerRequest request) {
        return CustomerMapper.toResponse(customerUseCases.createCustomer(CustomerMapper.toNewDomain(request)));
    }

    @GetMapping
    @Operation(summary = "List customers",
            description = "Returns a paged list of customers. When `search` is provided it filters by first name, "
                    + "last name or email, case insensitive.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged list of customers",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public PageResponse<CustomerResponse> listCustomers(
            @Parameter(description = "Optional text to filter by name or email", example = "mary") @RequestParam(required = false) String search,
            @Parameter(description = "Zero based page index", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Maximum number of items per page", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort property and direction, format `property,direction`.",
                    example = "id,asc") @RequestParam(defaultValue = "id,asc") String sort) {
        var query = PaginationSupport.of(page, size, sort);
        var result = search == null || search.isBlank()
                ? customerUseCases.listCustomers(query)
                : customerUseCases.searchCustomers(search, query);
        return PageMapper.map(result, CustomerMapper::toResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a customer by identifier",
            description = "Returns the customer detail including its address. Responds with 404 when the customer "
                    + "does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer detail",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public CustomerResponse getCustomer(
            @Parameter(description = "Identifier of the customer", example = "1") @PathVariable Integer id) {
        return CustomerMapper.toResponse(customerUseCases.getCustomer(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Update a customer",
            description = "Updates the name, email and address of an existing customer. The email must not be used by "
                    + "another customer. Responds with 409 on a duplicate email and 404 when the customer or address "
                    + "does not exist. Requires the ADMIN or EMPLOYEE role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without the required role",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer or address not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already registered by another customer",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public CustomerResponse updateCustomer(@Parameter(description = "Identifier of the customer", example = "1")
            @PathVariable Integer id, @Valid @RequestBody CustomerUpdateRequest request) {
        Customer current = customerUseCases.getCustomer(id);
        return CustomerMapper.toResponse(
                customerUseCases.updateCustomer(id, CustomerMapper.toUpdateDomain(id, current, request)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Activate or deactivate a customer",
            description = "Changes the active status of a customer. A deactivated customer keeps its rental history "
                    + "but cannot rent new films. Responds with 404 when the customer does not exist. "
                    + "Requires the ADMIN or EMPLOYEE role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer status updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without the required role",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public CustomerResponse changeCustomerStatus(@Parameter(description = "Identifier of the customer", example = "1")
            @PathVariable Integer id, @Valid @RequestBody CustomerStatusRequest request) {
        return CustomerMapper.toResponse(customerUseCases.changeCustomerStatus(id, request.active()));
    }

    @GetMapping("/{id}/rentals")
    @Operation(summary = "List the rental history of a customer",
            description = "Returns a paged list of all rentals of the given customer, including returned ones. "
                    + "Responds with 404 when the customer does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged list of customer rentals",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public PageResponse<RentalResponse> customerRentals(
            @Parameter(description = "Identifier of the customer", example = "1") @PathVariable Integer id,
            @Parameter(description = "Zero based page index", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Maximum number of items per page", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort property and direction, format `property,direction`.",
                    example = "rentalDate,desc") @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(customerUseCases.getCustomerRentals(id, PaginationSupport.of(page, size, sort)),
                RentalMapper::toResponse);
    }
}
