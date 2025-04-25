CREATE TABLE tags (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) UNIQUE NOT NULL,
                      type VARCHAR(50) NULL,
                      description TEXT NULL
);

CREATE TABLE task_tags (
                           task_id INT,
                           tag_id INT,
                           PRIMARY KEY (task_id, tag_id),
                           FOREIGN KEY (task_id) REFERENCES tasks(id),
                           FOREIGN KEY (tag_id) REFERENCES tags(id)
);