package com.sakila.api.domain.model;

public record LoginResult(String token, String username, String role) {
}
