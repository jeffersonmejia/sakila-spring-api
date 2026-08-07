package com.sakila.api.integration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Tag("integration")
public abstract class AbstractIntegrationTest {

    static final org.testcontainers.containers.PostgreSQLContainer<?> POSTGRES =
            new org.testcontainers.containers.PostgreSQLContainer<>("pgvector/pgvector:pg17")
                    .withDatabaseName("sakila_test")
                    .withUsername("sakila_test")
                    .withPassword("sakila_test")
                    .withInitScript("init-testdb.sql");

    static final Path BACKUP_DIR;

    static {
        POSTGRES.start();
        try {
            BACKUP_DIR = Files.createTempDirectory("sakila-backup-test");
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo crear el directorio temporal de backups", e);
        }
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("app.jwt.secret", () -> "test-secret-0123456789012345678901234567890123456789");
        registry.add("app.jwt.expiration-minutes", () -> "60");
        registry.add("app.backup.directory", BACKUP_DIR::toString);
    }

    protected String adminToken() throws Exception {
        return loginToken("admin", "admin123");
    }

    protected String employeeToken() throws Exception {
        return loginToken("employee", "employee123");
    }

    protected String loginToken(String username, String password) throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(body).get("token").asText();
    }

    protected MockHttpServletRequestBuilder getAuthenticated(String path, String token) {
        return MockMvcRequestBuilders.get(path).header("Authorization", "Bearer " + token);
    }

    protected ResultActions getJson(String path, String token) throws Exception {
        return mockMvc.perform(getAuthenticated(path, token).accept(MediaType.APPLICATION_JSON));
    }

    protected Integer queryForInteger(String sql) {
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    protected Long queryForLong(String sql) {
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    protected String queryForString(String sql) {
        return jdbcTemplate.queryForObject(sql, String.class);
    }
}
