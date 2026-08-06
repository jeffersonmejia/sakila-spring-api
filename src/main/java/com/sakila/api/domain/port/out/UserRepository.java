package com.sakila.api.domain.port.out;

import java.util.Optional;

import com.sakila.api.domain.model.User;

public interface UserRepository {

    Optional<User> findByUsername(String username);
}
