package com.sakila.api.adapter.in.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sakila.api.adapter.in.web.dto.ErrorResponse;
import com.sakila.api.adapter.in.web.dto.LoginRequest;
import com.sakila.api.adapter.in.web.dto.LoginResponse;
import com.sakila.api.domain.port.in.AuthUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User login and JWT token issuance. This endpoint does not require a token.")
public class AuthController {

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate and obtain a JWT",
            description = "Public endpoint. Validates the credentials and returns a JWT bearer token with the user role. "
                    + "Use the returned token with the Authorize button to access the protected endpoints. "
                    + "Test accounts: `admin` / `admin123` (ADMIN) and `employee` / `employee123` (EMPLOYEE).")
    @SecurityRequirements({})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Missing or invalid credentials in the body",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid user name or password",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        var result = authUseCase.login(request.username(), request.password());
        return new LoginResponse(result.token(), result.username(), result.role());
    }
}
