package com.sakila.api.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sakila.api.common.exception.InvalidCredentialsException;
import com.sakila.api.domain.model.LoginResult;
import com.sakila.api.domain.model.User;
import com.sakila.api.domain.port.in.AuthUseCase;
import com.sakila.api.domain.port.out.UserRepository;
import com.sakila.api.security.JwtService;

@Service
public class AuthService implements AuthUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResult login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));
        if (!Boolean.TRUE.equals(user.enabled()) || !passwordEncoder.matches(password, user.password())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }
        String token = jwtService.generate(user.username(), user.role());
        return new LoginResult(token, user.username(), user.role());
    }
}
