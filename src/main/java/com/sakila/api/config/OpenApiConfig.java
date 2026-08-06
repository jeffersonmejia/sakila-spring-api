package com.sakila.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sakilaOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Sakila Backend API")
                        .description("""
                                REST API for the Sakila sample video rental store, built with a hexagonal
                                architecture. The API exposes films, categories, customers, inventory,
                                rentals, reports, authentication and database backups.

                                ## Authentication
                                All endpoints except **POST /api/auth/login**, the OpenAPI docs and the
                                health check require a JWT bearer token. Call **POST /api/auth/login**,
                                copy the returned `token` and press the **Authorize** button to attach it
                                to every request.

                                Default test accounts:
                                - **ADMIN** — username `admin`, password `admin123`
                                - **EMPLOYEE** — username `employee`, password `employee123`

                                ## Roles
                                - **ADMIN** can create customers, rentals and generate database backups.
                                - **EMPLOYEE** can create customers and rentals.
                                - Reads are available to any authenticated user.

                                ## Common error responses
                                - `400 Bad Request` — invalid request body or missing query parameters
                                - `401 Unauthorized` — missing, expired or invalid token, or bad credentials
                                - `403 Forbidden` — authenticated but without the required role
                                - `404 Not Found` — resource does not exist
                                - `409 Conflict` — business rule violation (duplicate email, occupied copy, double return)
                                - `500 Internal Server Error` — unexpected failure
                                """)
                        .version("1.0.0")
                        .contact(new Contact().name("Sakila Backend API")
                                .url("https://github.com/jeffersonmejia/sakila-spring-api"))
                        .license(new License().name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .components(new Components().addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT returned by **POST /api/auth/login**.")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}
