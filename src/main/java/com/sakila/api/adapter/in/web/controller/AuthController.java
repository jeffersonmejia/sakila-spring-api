package com.sakila.api.adapter.in.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sakila.api.adapter.in.web.dto.LoginRequest;
import com.sakila.api.adapter.in.web.dto.LoginResponse;
import com.sakila.api.domain.port.in.AuthUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        var result = authUseCase.login(request.username(), request.password());
        return new LoginResponse(result.token(), result.username(), result.role());
    }
}
