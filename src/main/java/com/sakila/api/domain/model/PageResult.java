package com.sakila.api.domain.model;

import java.util.List;

public record PageResult<T>(List<T> content, int page, int size, long totalElements, int totalPages, boolean first,
        boolean last) {

    public static <T> PageResult<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        boolean first = page == 0;
        boolean last = page >= totalPages - 1;
        return new PageResult<>(content, page, size, totalElements, totalPages, first, last);
    }
}
