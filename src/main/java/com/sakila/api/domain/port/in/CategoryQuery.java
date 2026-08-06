package com.sakila.api.domain.port.in;

import com.sakila.api.domain.model.Category;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;

public interface CategoryQuery {

    PageResult<Category> listCategories(PageQuery query);

    PageResult<Film> filmsByCategory(Integer categoryId, PageQuery query);
}
