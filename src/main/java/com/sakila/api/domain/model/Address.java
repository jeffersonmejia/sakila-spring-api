package com.sakila.api.domain.model;

public record Address(Integer id, String address, String district, String postalCode, String phone, City city) {
}
