package com.sakila.api.adapter.in.web.dto;

import java.time.LocalDate;

public record CustomerResponse(
        Integer id,
        String firstName,
        String lastName,
        String email,
        Boolean active,
        Integer storeId,
        LocalDate createDate,
        AddressResponse address) {
}
