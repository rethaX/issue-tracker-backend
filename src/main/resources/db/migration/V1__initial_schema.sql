CREATE TYPE issue_status AS ENUM ('OPEN', 'IN_PROGRESS', 'CLOSED');
CREATE TYPE issue_priority AS ENUM ('LOW', 'MEDIUM', 'HIGH');

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE issues (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
                        status issue_status NOT NULL DEFAULT 'OPEN',
                        priority issue_priority NOT NULL DEFAULT 'LOW',
                        user_id UUID REFERENCES users(id) ON DELETE CASCADE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);