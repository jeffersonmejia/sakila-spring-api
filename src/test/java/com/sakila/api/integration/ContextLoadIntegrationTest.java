package com.sakila.api.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

class ContextLoadIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoadsAndMigrationsApplied() {
        Integer films = jdbcTemplate.queryForObject("select count(*) from film", Integer.class);
        assertNotNull(films);
        org.junit.jupiter.api.Assertions.assertTrue(films > 0, "El seed de films debe haberse aplicado");
    }
}
