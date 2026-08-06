package com.sakila.api.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sakila.api.common.exception.InvalidCredentialsException;
import com.sakila.api.domain.model.LoginResult;
import com.sakila.api.domain.model.User;
import com.sakila.api.domain.port.out.UserRepository;
import com.sakila.api.domain.service.AuthService;
import com.sakila.api.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private final User admin = new User(1L, "admin", "hash", "ADMIN", true);

    @Test
    void loginWithValidCredentialsReturnsToken() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("admin123", "hash")).thenReturn(true);
        when(jwtService.generate("admin", "ADMIN")).thenReturn("jwt-token");

        LoginResult result = authService.login("admin", "admin123");

        assertEquals("jwt-token", result.token());
        assertEquals("admin", result.username());
        assertEquals("ADMIN", result.role());
        verify(jwtService).generate("admin", "ADMIN");
    }

    @Test
    void loginWithUnknownUserThrows() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        InvalidCredentialsException ex = assertThrows(InvalidCredentialsException.class,
                () -> authService.login("ghost", "password1"));

        assertEquals("Credenciales inválidas", ex.getMessage());
        verify(jwtService, never()).generate(any(), any());
    }

    @Test
    void loginWithDisabledUserThrows() {
        User disabled = new User(1L, "admin", "hash", "ADMIN", false);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(disabled));

        InvalidCredentialsException ex = assertThrows(InvalidCredentialsException.class,
                () -> authService.login("admin", "admin123"));

        assertEquals("Credenciales inválidas", ex.getMessage());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtService, never()).generate(any(), any());
    }

    @Test
    void loginWithWrongPasswordThrows() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("admin123", "hash")).thenReturn(false);

        InvalidCredentialsException ex = assertThrows(InvalidCredentialsException.class,
                () -> authService.login("admin", "admin123"));

        assertEquals("Credenciales inválidas", ex.getMessage());
        verify(jwtService, never()).generate(any(), any());
    }
}
