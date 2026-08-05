# Sakila Backend API

API REST para la gestión de películas, clientes, inventario, alquileres y devoluciones sobre la base de datos Sakila.

## Tecnologías

Java 21 · Spring Boot 3 · Spring Data JPA · Spring Security + JWT · PostgreSQL · Flyway · Springdoc OpenAPI · Testcontainers · GitHub Actions.

## Arquitectura

API hexagonal (Ports & Adapters): el núcleo de negocio no depende de Spring, JPA, HTTP ni JWT; los adaptadores de entrada y salida dependen del dominio.

```mermaid
flowchart TB
  subgraph Users["Usuarios"]
    A[Cliente]
    B[Administrador ADMIN]
    C[Empleado EMPLOYEE]
  end

  subgraph security["security — JWT & Roles"]
    N[JWT Filter · BCrypt · ADMIN / EMPLOYEE]
  end

  subgraph adapters_in["adapter/in/web — Entrada"]
    D[Controllers REST]
    E[DTOs + Validation]
    F[Exception Handler]
  end

  subgraph core["domain — Núcleo de negocio"]
    G[Puertos in — Casos de uso]
    I[Servicios de dominio]
    J[Modelos]
    H[Puertos out — Repositorios]
  end

  subgraph adapters_out["adapter/out/persistence — Salida"]
    K[Entidades JPA]
    L[Spring Data Repositories]
  end

  M[(PostgreSQL Sakila)]

  A --> D
  B --> D
  C --> D

  D --> G
  D --> F
  G --> I
  I --> H
  I --> J

  N --> H

  H --> L
  L --> K
  K --> M
```

## Ejecución local

```bash
# Configurar credenciales de la base de datos
cp .env.example .env

# Compilar
./mvnw clean package

# Ejecutar
java -jar target/sakila-api.jar
```

## Documentación de la API

- Swagger UI: `/swagger-ui/index.html`
- OpenAPI: `/v3/api-docs`
- Health: `/actuator/health`
