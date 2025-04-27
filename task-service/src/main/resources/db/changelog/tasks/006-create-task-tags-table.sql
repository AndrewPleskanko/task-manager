CREATE TABLE task_tags (
                           task_id INT,
                           tag_id INT,
                           PRIMARY KEY (task_id, tag_id),
                           FOREIGN KEY (task_id) REFERENCES tasks(id),
                           FOREIGN KEY (tag_id) REFERENCES tags(id)
);