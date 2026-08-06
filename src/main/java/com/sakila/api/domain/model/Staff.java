package com.sakila.api.domain.model;

public record Staff(Integer id, String firstName, String lastName, String email, String username, Boolean active) {
}
