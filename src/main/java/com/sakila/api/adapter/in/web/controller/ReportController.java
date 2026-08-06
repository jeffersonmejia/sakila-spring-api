package com.sakila.api.adapter.in.web.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sakila.api.adapter.in.web.dto.ActiveRentalsByStoreResponse;
import com.sakila.api.adapter.in.web.dto.ErrorResponse;
import com.sakila.api.adapter.in.web.dto.MostRentedFilmResponse;
import com.sakila.api.adapter.in.web.dto.RentalsByMonthResponse;
import com.sakila.api.adapter.in.web.dto.RevenueByCategoryResponse;
import com.sakila.api.adapter.in.web.dto.TopCustomerResponse;
import com.sakila.api.domain.port.in.ReportQuery;
import com.sakila.api.mapper.ReportMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Business reports over rentals, customers and revenue. Any authenticated user can query them.")
public class ReportController {

    private final ReportQuery reportQuery;

    public ReportController(ReportQuery reportQuery) {
        this.reportQuery = reportQuery;
    }

    @GetMapping("/most-rented-films")
    @Operation(summary = "Most rented films",
            description = "Returns the films with the highest rental count, ordered from most to least rented.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of most rented films",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MostRentedFilmResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public List<MostRentedFilmResponse> mostRentedFilms(
            @Parameter(description = "Maximum number of films to return", example = "10") @RequestParam(defaultValue = "10") int limit) {
        return reportQuery.mostRentedFilms(limit).stream().map(ReportMapper::toMostRented).toList();
    }

    @GetMapping("/top-customers")
    @Operation(summary = "Top customers",
            description = "Returns the customers with the highest number of rentals, ordered from highest to lowest.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of top customers",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TopCustomerResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public List<TopCustomerResponse> topCustomers(
            @Parameter(description = "Maximum number of customers to return", example = "10") @RequestParam(defaultValue = "10") int limit) {
        return reportQuery.topCustomers(limit).stream().map(ReportMapper::toTopCustomer).toList();
    }

    @GetMapping("/revenue-by-category")
    @Operation(summary = "Revenue by category",
            description = "Returns the accumulated revenue of each film category, ordered by revenue.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of revenues by category",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RevenueByCategoryResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public List<RevenueByCategoryResponse> revenueByCategory() {
        return reportQuery.revenueByCategory().stream().map(ReportMapper::toRevenue).toList();
    }

    @GetMapping("/rentals-by-month")
    @Operation(summary = "Rentals by month",
            description = "Returns the number of rentals registered in each month, ordered chronologically.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of rental counts by month",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RentalsByMonthResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public List<RentalsByMonthResponse> rentalsByMonth() {
        return reportQuery.rentalsByMonth().stream().map(ReportMapper::toRentalsByMonth).toList();
    }

    @GetMapping("/active-rentals-by-store")
    @Operation(summary = "Active rentals by store",
            description = "Returns the number of currently active rentals for each store.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of active rental counts by store",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ActiveRentalsByStoreResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public List<ActiveRentalsByStoreResponse> activeRentalsByStore() {
        return reportQuery.activeRentalsByStore().stream().map(ReportMapper::toActiveByStore).toList();
    }
}
