CREATE TABLE tags (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) UNIQUE NOT NULL,
                      type VARCHAR(50) NULL,
                      description TEXT NULL
);