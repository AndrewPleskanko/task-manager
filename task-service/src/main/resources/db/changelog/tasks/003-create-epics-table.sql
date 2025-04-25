CREATE TABLE epics
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP             DEFAULT NULL
);
