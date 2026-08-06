package com.sakila.api.domain.model;

import java.math.BigDecimal;

public record RevenueByCategory(String category, BigDecimal revenue) {
}
