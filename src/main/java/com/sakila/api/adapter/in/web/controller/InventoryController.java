package com.sakila.api.adapter.in.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sakila.api.adapter.in.web.PaginationSupport;
import com.sakila.api.adapter.in.web.dto.AvailabilityResponse;
import com.sakila.api.adapter.in.web.dto.ErrorResponse;
import com.sakila.api.adapter.in.web.dto.InventoryResponse;
import com.sakila.api.adapter.in.web.dto.PageResponse;
import com.sakila.api.domain.port.in.InventoryQuery;
import com.sakila.api.mapper.InventoryMapper;
import com.sakila.api.mapper.PageMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Inventory", description = "Inventory copies and availability queries. Any authenticated user can query them.")
public class InventoryController {

    private final InventoryQuery inventoryQuery;

    public InventoryController(InventoryQuery inventoryQuery) {
        this.inventoryQuery = inventoryQuery;
    }

    @GetMapping("/inventory")
    @Operation(summary = "List inventory copies",
            description = "Returns a paged list of all physical copies with their availability.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged list of inventory copies",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public PageResponse<InventoryResponse> listInventory(
            @Parameter(description = "Zero based page index", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Maximum number of items per page", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort property and direction, format `property,direction`.",
                    example = "id,asc") @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(inventoryQuery.listInventory(PaginationSupport.of(page, size, sort)),
                InventoryMapper::toResponse);
    }

    @GetMapping("/inventory/{id}")
    @Operation(summary = "Get an inventory copy by identifier",
            description = "Returns the copy with its film, store and availability. Responds with 404 when the copy "
                    + "does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventory copy detail",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventoryResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Inventory copy not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public InventoryResponse getInventory(
            @Parameter(description = "Identifier of the inventory copy", example = "1") @PathVariable Integer id) {
        return InventoryMapper.toResponse(inventoryQuery.getInventory(id));
    }

    @GetMapping("/films/{filmId}/inventory")
    @Operation(summary = "List the copies of a film",
            description = "Returns a paged list of the copies of the given film across all stores. "
                    + "Responds with 404 when the film does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged list of copies of the film",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Film not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public PageResponse<InventoryResponse> inventoryByFilm(
            @Parameter(description = "Identifier of the film", example = "1") @PathVariable Integer filmId,
            @Parameter(description = "Zero based page index", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Maximum number of items per page", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort property and direction, format `property,direction`.",
                    example = "id,asc") @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(inventoryQuery.inventoryByFilm(filmId, PaginationSupport.of(page, size, sort)),
                InventoryMapper::toResponse);
    }

    @GetMapping("/films/{filmId}/availability")
    @Operation(summary = "Get the availability of a film",
            description = "Returns the total, available and rented copy counts for the given film. "
                    + "Responds with 404 when the film does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Availability summary of the film",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AvailabilityResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Film not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public AvailabilityResponse availability(
            @Parameter(description = "Identifier of the film", example = "1") @PathVariable Integer filmId) {
        var availability = inventoryQuery.availability(filmId);
        return InventoryMapper.toAvailability(availability.film(), availability.totalCopies(), availability.availableCopies());
    }
}
