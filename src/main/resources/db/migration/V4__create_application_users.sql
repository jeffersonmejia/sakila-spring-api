-- Application users for authentication (Spring Security).
-- Passwords are BCrypt hashes.

CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    last_update TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

-- Default users (change passwords in production):
--   admin     / admin123     role ADMIN
--   employee  / employee123  role EMPLOYEE
INSERT INTO app_user (username, password, role, enabled) VALUES
    ('admin', '$2b$10$h44Pn.9rpmKlSAmk7B7ATOZaBhtT61ewPz6T.K1EYQOvUzhglNJSm', 'ADMIN', TRUE),
    ('employee', '$2b$10$weEnKfsQe1Ho/OXa4QrUB.n854YwfH6diuAjQlsy12jjq2zaJ7KOy', 'EMPLOYEE', TRUE);
