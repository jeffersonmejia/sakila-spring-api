package com.sakila.api.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sakila.api.adapter.out.persistence.mapper.EntityMapper;
import com.sakila.api.adapter.out.persistence.repository.JpaUserRepository;
import com.sakila.api.domain.model.User;
import com.sakila.api.domain.port.out.UserRepository;

@Repository
@Transactional(readOnly = true)
public class UserPersistenceAdapter implements UserRepository {

    private final JpaUserRepository jpa;

    public UserPersistenceAdapter(JpaUserRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpa.findByUsername(username).map(EntityMapper::toUser);
    }
}
