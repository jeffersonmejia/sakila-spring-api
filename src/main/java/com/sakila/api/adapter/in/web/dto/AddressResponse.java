package com.sakila.api.adapter.in.web.dto;

public record AddressResponse(Integer id, String address, String district, String postalCode, String phone,
        CityResponse city) {
}
