package com.sakila.api.mapper;

import java.util.List;
import java.util.function.Function;

import com.sakila.api.adapter.in.web.dto.PageResponse;
import com.sakila.api.domain.model.PageResult;

public final class PageMapper {

    private PageMapper() {
    }

    public static <T, D> PageResponse<D> map(PageResult<T> page, Function<T, D> converter) {
        List<D> content = page.content().stream().map(converter).toList();
        return new PageResponse<>(content, page.page(), page.size(), page.totalElements(), page.totalPages(),
                page.first(), page.last());
    }
}
