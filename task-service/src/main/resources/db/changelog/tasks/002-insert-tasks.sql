INSERT INTO tasks (title, description, status, created_at, updated_at, user_id)
VALUES
    ('Task 1', 'Description for Task 1', 'OPEN', CURRENT_TIMESTAMP, NULL, 1),
    ('Task 2', 'Description for Task 2', 'IN_PROGRESS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
    ('Task 3', 'Description for Task 3', 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
