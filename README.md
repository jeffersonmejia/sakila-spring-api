# Sakila Backend API

API REST para la gestión de películas, clientes, inventario, alquileres y devoluciones sobre la base de datos Sakila.

## Tecnologías

Java 21 · Spring Boot 3 · Spring Data JPA · Spring Security + JWT · PostgreSQL · Flyway · Springdoc OpenAPI · Testcontainers · GitHub Actions.

## Prerrequisitos

- PostgreSQL 17 con la extensión `vector` (pgvector) instalada.
- La base `sakila` se crea y se puebla ejecutando en orden los scripts de `.local/db/`: `001_schema.sql`, `002_seed.sql` y `003_users.sql`.

## Arquitectura

API hexagonal (Ports & Adapters): el núcleo de negocio no depende de Spring, JPA, HTTP ni JWT; los adaptadores de entrada y salida dependen del dominio.

```mermaid
flowchart TB
  classDef userCls fill:#E3F2FD,stroke:#1565C0,color:#0D47A1,stroke-width:2px
  classDef secCls fill:#FCE4EC,stroke:#C2185B,color:#880E4F,stroke-width:2px
  classDef inCls fill:#FFF3E0,stroke:#EF6C00,color:#E65100,stroke-width:2px
  classDef coreCls fill:#E8F5E9,stroke:#2E7D32,color:#1B5E20,stroke-width:2px
  classDef outCls fill:#F3E5F5,stroke:#8E24AA,color:#4A148C,stroke-width:2px
  classDef dbCls fill:#FFFDE7,stroke:#F9A825,color:#F57F17,stroke-width:2px

  subgraph Users["Users"]
    A[Client]
    B[Administrator ADMIN]
    C[Employee EMPLOYEE]
  end

  subgraph security["security — JWT & Roles"]
    N[JWT Filter · BCrypt · ADMIN / EMPLOYEE]
  end

  subgraph adapters_in["adapter/in/Application — Input"]
    D[REST Controllers]
    E[DTOs + Validation]
    F[Exception Handler]
  end

  subgraph core["domain — Business Core"]
    G[In Ports — Use Cases]
    I[Domain Services]
    J[Models]
    H[Out Ports — Repositories]
  end

  subgraph adapters_out["adapter/out/Infraestructure — Output"]
    K[JPA Entities]
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

  class A,B,C userCls
  class N secCls
  class D,E,F inCls
  class G,I,J,H coreCls
  class K,L outCls
  class M dbCls
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
