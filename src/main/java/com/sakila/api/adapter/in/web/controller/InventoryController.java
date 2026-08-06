package com.sakila.api.adapter.in.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sakila.api.adapter.in.web.PaginationSupport;
import com.sakila.api.adapter.in.web.dto.AvailabilityResponse;
import com.sakila.api.adapter.in.web.dto.InventoryResponse;
import com.sakila.api.adapter.in.web.dto.PageResponse;
import com.sakila.api.domain.port.in.InventoryQuery;
import com.sakila.api.mapper.InventoryMapper;
import com.sakila.api.mapper.PageMapper;

@RestController
@RequestMapping("/api")
public class InventoryController {

    private final InventoryQuery inventoryQuery;

    public InventoryController(InventoryQuery inventoryQuery) {
        this.inventoryQuery = inventoryQuery;
    }

    @GetMapping("/inventory")
    public PageResponse<InventoryResponse> listInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(inventoryQuery.listInventory(PaginationSupport.of(page, size, sort)),
                InventoryMapper::toResponse);
    }

    @GetMapping("/inventory/{id}")
    public InventoryResponse getInventory(@PathVariable Integer id) {
        return InventoryMapper.toResponse(inventoryQuery.getInventory(id));
    }

    @GetMapping("/films/{filmId}/inventory")
    public PageResponse<InventoryResponse> inventoryByFilm(
            @PathVariable Integer filmId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        return PageMapper.map(inventoryQuery.inventoryByFilm(filmId, PaginationSupport.of(page, size, sort)),
                InventoryMapper::toResponse);
    }

    @GetMapping("/films/{filmId}/availability")
    public AvailabilityResponse availability(@PathVariable Integer filmId) {
        var availability = inventoryQuery.availability(filmId);
        return InventoryMapper.toAvailability(availability.film(), availability.totalCopies(), availability.availableCopies());
    }
}
