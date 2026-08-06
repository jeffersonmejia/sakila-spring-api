package com.sakila.api.domain.model;

import java.time.LocalDate;

public record Customer(
        Integer id,
        String firstName,
        String lastName,
        String email,
        Boolean active,
        Integer storeId,
        Address address,
        LocalDate createDate) {
}
