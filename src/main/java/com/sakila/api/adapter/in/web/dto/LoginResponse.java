package com.sakila.api.adapter.in.web.dto;

public record LoginResponse(String token, String username, String role) {
}
