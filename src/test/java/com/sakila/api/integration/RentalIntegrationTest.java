package com.sakila.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class RentalIntegrationTest extends AbstractIntegrationTest {

    private int freeCopyId() {
        return queryForInteger("select i.inventory_id from inventory i where not exists "
                + "(select 1 from rental r where r.inventory_id = i.inventory_id and r.return_date is null) "
                + "order by i.inventory_id limit 1");
    }

    private int activeCustomerId() {
        return queryForInteger("select customer_id from customer where activebool = true order by customer_id limit 1");
    }

    private int inactiveCustomerId() {
        return queryForInteger("select customer_id from customer where activebool = false order by customer_id limit 1");
    }

    private int staffId() {
        return queryForInteger("select staff_id from staff where staff_id > 0 order by staff_id limit 1");
    }

    private int createRental(int customerId, int inventoryId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/rentals")
                .header("Authorization", "Bearer " + employeeToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerId\":" + customerId + ",\"inventoryId\":" + inventoryId
                        + ",\"staffId\":" + staffId() + "}"))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.get("id").asInt();
    }

    @Test
    void createValidRentalReturnsCreated() throws Exception {
        int customerId = activeCustomerId();
        int inventoryId = freeCopyId();
        int staff = staffId();

        mockMvc.perform(post("/api/rentals")
                .header("Authorization", "Bearer " + employeeToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerId\":" + customerId + ",\"inventoryId\":" + inventoryId
                        + ",\"staffId\":" + staff + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.rentalDate").exists())
                .andExpect(jsonPath("$.returnDate").doesNotExist())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.inventoryId").value(inventoryId))
                .andExpect(jsonPath("$.filmTitle").exists())
                .andExpect(jsonPath("$.staffId").value(staff))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void rentToInactiveCustomerReturnsConflict() throws Exception {
        int customerId = inactiveCustomerId();
        int inventoryId = freeCopyId();

        mockMvc.perform(post("/api/rentals")
                .header("Authorization", "Bearer " + employeeToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerId\":" + customerId + ",\"inventoryId\":" + inventoryId
                        + ",\"staffId\":" + staffId() + "}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El cliente no está activo"));
    }

    @Test
    void rentToNonexistentCustomerReturnsNotFound() throws Exception {
        mockMvc.perform(post("/api/rentals")
                .header("Authorization", "Bearer " + employeeToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerId\":999999,\"inventoryId\":" + freeCopyId()
                        + ",\"staffId\":" + staffId() + "}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente no encontrado"));
    }

    @Test
    void rentOccupiedCopyReturnsConflict() throws Exception {
        int customerId = activeCustomerId();
        int inventoryId = freeCopyId();

        createRental(customerId, inventoryId);

        mockMvc.perform(post("/api/rentals")
                .header("Authorization", "Bearer " + employeeToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerId\":" + customerId + ",\"inventoryId\":" + inventoryId
                        + ",\"staffId\":" + staffId() + "}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("La copia seleccionada no está disponible"));
    }

    @Test
    void returnActiveRentalReturnsOk() throws Exception {
        int rentalId = createRental(activeCustomerId(), freeCopyId());

        mockMvc.perform(post("/api/rentals/" + rentalId + "/return")
                .header("Authorization", "Bearer " + employeeToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rentalId))
                .andExpect(jsonPath("$.returnDate").exists())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void returnRentalTwiceReturnsConflict() throws Exception {
        int rentalId = createRental(activeCustomerId(), freeCopyId());

        mockMvc.perform(post("/api/rentals/" + rentalId + "/return")
                .header("Authorization", "Bearer " + employeeToken()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/rentals/" + rentalId + "/return")
                .header("Authorization", "Bearer " + employeeToken()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El alquiler ya fue devuelto"));
    }

    @Test
    void returnNonexistentRentalReturnsNotFound() throws Exception {
        mockMvc.perform(post("/api/rentals/999999/return")
                .header("Authorization", "Bearer " + employeeToken()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Alquiler no encontrado"));
    }

    @Test
    void getRentalReturnsDetail() throws Exception {
        int rentalId = createRental(activeCustomerId(), freeCopyId());

        mockMvc.perform(get("/api/rentals/" + rentalId).header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rentalId))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void activeRentalsReturnPagedContent() throws Exception {
        mockMvc.perform(get("/api/rentals/active").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void overdueRentalsReturnPagedContent() throws Exception {
        mockMvc.perform(get("/api/rentals/overdue").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
