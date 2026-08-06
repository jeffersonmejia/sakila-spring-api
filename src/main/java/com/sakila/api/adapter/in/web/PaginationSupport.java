package com.sakila.api.adapter.in.web;

import java.time.LocalDateTime;

import org.springframework.data.domain.Sort;

import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.SortDirection;

public final class PaginationSupport {

    private PaginationSupport() {
    }

    public static PageQuery of(int page, int size, String sort) {
        String property = "id";
        SortDirection direction = SortDirection.ASC;
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            property = parts[0].trim();
            if (parts.length > 1) {
                direction = "desc".equalsIgnoreCase(parts[1].trim()) ? SortDirection.DESC : SortDirection.ASC;
            }
        }
        return PageQuery.of(page, size, property, direction);
    }
}
