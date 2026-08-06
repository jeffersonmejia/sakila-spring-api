package com.sakila.api.domain.model;

public record TopCustomer(Integer customerId, String name, Long rentalsCount) {
}
