package com.sakila.api.domain.model;

public record Inventory(Integer id, Integer filmId, String filmTitle, Integer storeId, Boolean available) {
}
