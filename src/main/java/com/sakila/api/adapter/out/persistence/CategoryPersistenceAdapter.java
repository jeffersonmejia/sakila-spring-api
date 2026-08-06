package com.sakila.api.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sakila.api.adapter.out.persistence.entity.CategoryEntity;
import com.sakila.api.adapter.out.persistence.mapper.EntityMapper;
import com.sakila.api.adapter.out.persistence.mapper.PageResultMapper;
import com.sakila.api.adapter.out.persistence.repository.JpaCategoryRepository;
import com.sakila.api.domain.model.Category;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.SortDirection;
import com.sakila.api.domain.port.out.CategoryRepository;

@Repository
@Transactional(readOnly = true)
public class CategoryPersistenceAdapter implements CategoryRepository {

    private static final List<String> SORTABLE = List.of("id", "name");

    private final JpaCategoryRepository jpa;

    public CategoryPersistenceAdapter(JpaCategoryRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Category> findById(Integer id) {
        return jpa.findById(id).map(EntityMapper::toCategory);
    }

    @Override
    public PageResult<Category> findAll(PageQuery query) {
        Page<CategoryEntity> page = jpa.findAll(pageable(query));
        return PageResultMapper.map(page, EntityMapper::toCategory);
    }

    private Pageable pageable(PageQuery query) {
        String property = SORTABLE.contains(query.sortProperty()) ? query.sortProperty() : "id";
        Sort.Direction direction = query.direction() == SortDirection.DESC ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(query.page(), query.size(), Sort.by(direction, property));
    }
}
