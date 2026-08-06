package com.sakila.api.domain.model;

public record ActiveRentalsByStore(Integer storeId, Long activeCount) {
}
