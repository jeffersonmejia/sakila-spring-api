package com.sakila.api.adapter.in.web.dto;

public record InventoryResponse(Integer id, Integer filmId, String filmTitle, Integer storeId, boolean available) {
}
