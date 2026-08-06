package com.sakila.api.adapter.in.web.dto;

import java.math.BigDecimal;

public record RevenueByCategoryResponse(String category, BigDecimal revenue) {
}
