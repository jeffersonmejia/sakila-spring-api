package com.sakila.api.domain.model;

public record User(Long id, String username, String password, String role, Boolean enabled) {
}
