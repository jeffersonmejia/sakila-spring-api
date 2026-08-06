package com.sakila.api.domain.port.out;

import java.util.Optional;

import com.sakila.api.domain.model.Category;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;

public interface CategoryRepository {

    Optional<Category> findById(Integer id);

    PageResult<Category> findAll(PageQuery query);
}
