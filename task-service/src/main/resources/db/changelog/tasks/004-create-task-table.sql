CREATE TABLE tasks
(
    id                 SERIAL PRIMARY KEY,
    title              VARCHAR(255) NOT NULL,
    description        TEXT         NOT NULL,
    status             VARCHAR(50)  NOT NULL,
    created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP             DEFAULT NULL,
    user_id            INT          NOT NULL,
    assigned_to        INT                   DEFAULT NULL,
    due_date           TIMESTAMP             DEFAULT NULL,
    priority           VARCHAR(20)           DEFAULT NULL,
    tags               TEXT                  DEFAULT NULL,
    completed_at       TIMESTAMP             DEFAULT NULL,
    user_story_id      BIGINT       NOT NULL,
    blocked_by_task_id INT                   DEFAULT NULL,
    FOREIGN KEY (user_story_id) REFERENCES user_stories (id),
    FOREIGN KEY (blocked_by_task_id) REFERENCES tasks (id)
);