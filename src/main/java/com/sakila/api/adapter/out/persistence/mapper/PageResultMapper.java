package com.sakila.api.adapter.out.persistence.mapper;

import org.springframework.data.domain.Page;

import java.util.function.Function;

import com.sakila.api.domain.model.PageResult;

public final class PageResultMapper {

    private PageResultMapper() {
    }

    public static <E, T> PageResult<T> map(Page<E> page, Function<E, T> converter) {
        return PageResult.of(page.getContent().stream().map(converter).toList(),
                page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
