CREATE TABLE user_stories
(
    id                  SERIAL PRIMARY KEY,
    title               VARCHAR(255) NOT NULL,
    description         TEXT         NOT NULL,
    status              VARCHAR(50)  NOT NULL DEFAULT 'BACKLOG',
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP             DEFAULT NULL,
    acceptance_criteria TEXT                  DEFAULT NULL,
    story_points        INT                   DEFAULT NULL,
    priority            INT                   DEFAULT NULL,
    epic_id             BIGINT                DEFAULT NULL,
    tags                TEXT                  DEFAULT NULL,
    project_id          BIGINT,
    FOREIGN KEY (epic_id) REFERENCES epics (id),
    FOREIGN KEY (project_id) REFERENCES projects (id)
);