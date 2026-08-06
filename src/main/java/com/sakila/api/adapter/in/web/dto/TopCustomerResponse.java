package com.sakila.api.adapter.in.web.dto;

public record TopCustomerResponse(Integer customerId, String name, Long rentalsCount) {
}
