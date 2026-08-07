package com.sakila.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.JsonNode;
import org.springframework.http.MediaType;

class AuthIntegrationTest extends AbstractIntegrationTest {

    @Test
    void loginWithValidCredentialsReturnsToken() throws Exception {
        String body = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"admin123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(body);
        org.junit.jupiter.api.Assertions.assertFalse(json.get("token").asText().isBlank());
    }

    @Test
    void loginWithInvalidPasswordReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"wrongpass\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Credenciales inválidas"));
    }

    @Test
    void loginWithUnknownUserReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"ghost\",\"password\":\"password1\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Credenciales inválidas"));
    }

    @Test
    void protectedEndpointWithoutTokenReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/films"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("No autenticado"));
    }

    @Test
    void employeeRoleCannotAccessAdminEndpoint() throws Exception {
        String token = employeeToken();
        mockMvc.perform(post("/api/backups").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Forbidden"))
                .andExpect(jsonPath("$.message").value("No tiene permiso para realizar esta operación"));
    }

    @Test
    void invalidTokenReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/films").header("Authorization", "Bearer not-a-jwt"))
                .andExpect(status().isUnauthorized());
    }
}
