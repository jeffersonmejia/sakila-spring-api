package com.sakila.api.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "JWT bearer token. Send it in the Authorization header as `Bearer <token>`.", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc1NDQzMzAwMCwiZXhwIjoxNzU0NDM2NjAwfQ.xyz123abc")
        String token,
        @Schema(description = "Authenticated user name", example = "admin")
        String username,
        @Schema(description = "Role of the authenticated user. One of `ADMIN` or `EMPLOYEE`.", example = "ADMIN")
        String role) {
}
