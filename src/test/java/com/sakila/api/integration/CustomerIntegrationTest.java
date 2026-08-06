package com.sakila.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;

class CustomerIntegrationTest extends AbstractIntegrationTest {

    @Test
    void registerValidCustomerReturnsCreated() throws Exception {
        int storeId = queryForInteger("select store_id from store where store_id > 0 order by store_id limit 1");
        int addressId = queryForInteger("select address_id from address order by address_id limit 1");
        String email = "test." + System.currentTimeMillis() + "@sakila.com";

        mockMvc.perform(post("/api/customers")
                .header("Authorization", "Bearer " + employeeToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"JUANA\",\"lastName\":\"PEREZ\",\"email\":\"" + email
                        + "\",\"storeId\":" + storeId + ",\"addressId\":" + addressId + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("JUANA"))
                .andExpect(jsonPath("$.lastName").value("PEREZ"))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.storeId").value(storeId))
                .andExpect(jsonPath("$.createDate").exists())
                .andExpect(jsonPath("$.address.id").value(addressId));
    }

    @Test
    void registerCustomerWithDuplicateEmailReturnsConflict() throws Exception {
        String email = queryForString(
                "select email from customer where email is not null and email <> '' limit 1");
        int storeId = queryForInteger("select store_id from store where store_id > 0 order by store_id limit 1");
        int addressId = queryForInteger("select address_id from address order by address_id limit 1");

        mockMvc.perform(post("/api/customers")
                .header("Authorization", "Bearer " + employeeToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"CARLOS\",\"lastName\":\"GOMEZ\",\"email\":\"" + email
                        + "\",\"storeId\":" + storeId + ",\"addressId\":" + addressId + "}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El correo electrónico ya está registrado"));
    }

    @Test
    void registerCustomerWithNonexistentStoreReturnsNotFound() throws Exception {
        int addressId = queryForInteger("select address_id from address order by address_id limit 1");
        String email = "nostore." + System.currentTimeMillis() + "@sakila.com";

        mockMvc.perform(post("/api/customers")
                .header("Authorization", "Bearer " + employeeToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"LUCIA\",\"lastName\":\"ROJAS\",\"email\":\"" + email
                        + "\",\"storeId\":999999,\"addressId\":" + addressId + "}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Tienda no encontrada"));
    }

    @Test
    void listCustomersReturnsPagedContent() throws Exception {
        mockMvc.perform(get("/api/customers").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20));
    }

    @Test
    void getExistingCustomerReturnsDetail() throws Exception {
        int id = queryForInteger("select customer_id from customer order by customer_id limit 1");

        mockMvc.perform(get("/api/customers/" + id).header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.address.id").exists());
    }

    @Test
    void getNonexistentCustomerReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/customers/999999").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente no encontrado"));
    }

    @Test
    void updateCustomerReturnsUpdatedData() throws Exception {
        int id = queryForInteger("select customer_id from customer order by customer_id limit 1");
        int addressId = queryForInteger("select address_id from address order by address_id limit 1");
        String email = "update." + System.currentTimeMillis() + "@sakila.com";

        mockMvc.perform(put("/api/customers/" + id)
                .header("Authorization", "Bearer " + employeeToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"MARIA\",\"lastName\":\"LOPEZ\",\"email\":\"" + email
                        + "\",\"addressId\":" + addressId + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value("MARIA"))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void changeCustomerStatusReturnsNewState() throws Exception {
        int id = queryForInteger("select customer_id from customer where activebool = true limit 1");

        mockMvc.perform(patch("/api/customers/" + id + "/status")
                .header("Authorization", "Bearer " + employeeToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"active\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void customerRentalsReturnPagedHistory() throws Exception {
        int id = queryForInteger("select customer_id from customer where customer_id in "
                + "(select distinct customer_id from rental) limit 1");

        mockMvc.perform(get("/api/customers/" + id + "/rentals")
                .header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
