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
import com.sakila.api.adapter.in.web.dto.ErrorResponse;
import com.sakila.api.adapter.in.web.dto.PageResponse;
import com.sakila.api.adapter.in.web.dto.RentalRequest;
import com.sakila.api.adapter.in.web.dto.RentalResponse;
import com.sakila.api.domain.model.Rental;
import com.sakila.api.domain.port.in.RentalUseCases;
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
@RequestMapping("/api/rentals")
@Tag(name = "Rentals", description = "Film rental operations. Writes require the ADMIN or EMPLOYEE role; reads are available to any authenticated user.")
public class RentalController {

    private final RentalUseCases rentalUseCases;

    public RentalController(RentalUseCases rentalUseCases) {
        this.rentalUseCases = rentalUseCases;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Register a new rental",
            description = "Rents a film copy to an active customer. The customer must exist and be active, the copy "
                    + "must exist and have no active rental. The rental date is set automatically. "
                    + "Responds with 404 for a missing customer or copy, and 409 for an inactive customer or an "
                    + "occupied copy. Requires the ADMIN or EMPLOYEE role.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Rental created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without the required role",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer or inventory copy not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Inactive customer or copy already rented",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public RentalResponse createRental(@Valid @RequestBody RentalRequest request) {
        Rental rental = new Rental(null, null, null, request.customerId(), request.inventoryId(), null, request.staffId());
        return RentalMapper.toResponse(rentalUseCases.createRental(rental));
    }

    @GetMapping("/active")
    @Operation(summary = "List active rentals",
            description = "Returns a paged list of all rentals that have not been returned yet.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged list of active rentals",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public PageResponse<RentalResponse> activeRentals(
            @Parameter(description = "Zero based page index", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Maximum number of items per page", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort property and direction, format `property,direction`.",
                    example = "rentalDate,desc") @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(rentalUseCases.listActive(PaginationSupport.of(page, size, sort)), RentalMapper::toResponse);
    }

    @GetMapping("/overdue")
    @Operation(summary = "List overdue rentals",
            description = "Returns a paged list of active rentals whose rental duration has been exceeded.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged list of overdue rentals",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public PageResponse<RentalResponse> overdueRentals(
            @Parameter(description = "Zero based page index", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Maximum number of items per page", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort property and direction, format `property,direction`.",
                    example = "rentalDate,asc") @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(rentalUseCases.listOverdue(PaginationSupport.of(page, size, sort)), RentalMapper::toResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a rental by identifier",
            description = "Returns the rental with its film title and return status. Responds with 404 when the "
                    + "rental does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rental detail",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Rental not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public RentalResponse getRental(
            @Parameter(description = "Identifier of the rental", example = "1") @PathVariable Integer id) {
        return RentalMapper.toResponse(rentalUseCases.getRental(id));
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Return a rented film",
            description = "Registers the return of an active rental. The return date is set automatically. "
                    + "Responds with 404 when the rental does not exist and 409 when it was already returned. "
                    + "Requires the ADMIN or EMPLOYEE role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rental returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without the required role",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Rental not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Rental already returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public RentalResponse returnRental(
            @Parameter(description = "Identifier of the rental to return", example = "1") @PathVariable Integer id) {
        return RentalMapper.toResponse(rentalUseCases.returnRental(id));
    }
}
