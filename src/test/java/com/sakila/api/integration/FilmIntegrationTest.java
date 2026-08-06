package com.sakila.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

class FilmIntegrationTest extends AbstractIntegrationTest {

    @Test
    void getExistingFilmReturnsDetail() throws Exception {
        int id = queryForInteger("select film_id from film order by film_id limit 1");
        String title = queryForString("select title from film where film_id = " + id);

        mockMvc.perform(get("/api/films/" + id).header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.film.id").value(id))
                .andExpect(jsonPath("$.film.title").value(title))
                .andExpect(jsonPath("$.actors").isArray())
                .andExpect(jsonPath("$.categories").isArray());
    }

    @Test
    void getNonexistentFilmReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/films/999999").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Película no encontrada"));
    }

    @Test
    void listFilmsReturnsPagedContent() throws Exception {
        mockMvc.perform(get("/api/films").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1000));
    }

    @Test
    void searchFilmsByTitleReturnsMatches() throws Exception {
        String title = queryForString("select title from film order by film_id limit 1");
        String fragment = title.length() > 4 ? title.substring(0, 4) : title;

        mockMvc.perform(get("/api/films/search?title=" + fragment)
                .header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void searchFilmsWithoutTitleReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/films/search").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void filmActorsReturnList() throws Exception {
        int id = queryForInteger("select film_id from film order by film_id limit 1");

        mockMvc.perform(get("/api/films/" + id + "/actors").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void filmCategoriesReturnList() throws Exception {
        int id = queryForInteger("select film_id from film order by film_id limit 1");

        mockMvc.perform(get("/api/films/" + id + "/categories").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void filmAvailabilityReturnsCounts() throws Exception {
        int id = queryForInteger("select film_id from film order by film_id limit 1");

        mockMvc.perform(get("/api/films/" + id + "/availability")
                .header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filmId").value(id))
                .andExpect(jsonPath("$.totalCopies").isNumber())
                .andExpect(jsonPath("$.availableCopies").isNumber())
                .andExpect(jsonPath("$.rentedCopies").isNumber());
    }

    @Test
    void listCategoriesReturnsPagedContent() throws Exception {
        mockMvc.perform(get("/api/categories").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(16));
    }

    @Test
    void filmsByCategoryReturnsPagedContent() throws Exception {
        mockMvc.perform(get("/api/categories/1/films").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void filmsByNonexistentCategoryReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/categories/999999/films").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categoría no encontrada"));
    }

    @Test
    void listInventoryReturnsPagedContent() throws Exception {
        mockMvc.perform(get("/api/inventory").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(4581));
    }

    @Test
    void getInventoryCopyReturnsDetail() throws Exception {
        int id = queryForInteger("select inventory_id from inventory order by inventory_id limit 1");

        mockMvc.perform(get("/api/inventory/" + id).header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.filmId").exists())
                .andExpect(jsonPath("$.storeId").exists());
    }

    @Test
    void inventoryByFilmReturnsCopies() throws Exception {
        int filmId = queryForInteger("select film_id from inventory order by inventory_id limit 1");

        mockMvc.perform(get("/api/films/" + filmId + "/inventory").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
