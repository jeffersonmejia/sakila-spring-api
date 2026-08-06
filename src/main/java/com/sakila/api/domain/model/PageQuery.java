package com.sakila.api.domain.model;

public record PageQuery(int page, int size, String sortProperty, SortDirection direction) {

    public static PageQuery of(int page, int size, String sortProperty, SortDirection direction) {
        return new PageQuery(Math.max(page, 0), Math.max(size, 1), sortProperty, direction);
    }
}
