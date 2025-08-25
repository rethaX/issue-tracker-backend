CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password_hash VARCHAR(255),
                       created_at TIMESTAMP NOT NULL
);

CREATE TYPE issue_status AS ENUM ('OPEN', 'IN_PROGRESS', 'CLOSED');
CREATE TYPE issue_priority AS ENUM ('LOW', 'MEDIUM', 'HIGH');

CREATE TABLE issues (
                        id UUID PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
                        status issue_status NOT NULL,
                        priority issue_priority NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP,
                        user_id UUID,
                        CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);