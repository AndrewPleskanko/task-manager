INSERT INTO tasks (title, description, status, created_at, updated_at, user_id, assigned_to, due_date, priority, tags,
                   completed_at)
VALUES ('Task 1', 'Description for Task 1', 'OPEN', CURRENT_TIMESTAMP, NULL, 1, 2, '2025-01-20 10:00:00', 'HIGH',
        'bug,urgent', NULL),
       ('Task 2', 'Description for Task 2', 'IN_PROGRESS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 3,
        '2025-02-15 15:00:00', 'MEDIUM', 'feature,backend', NULL),
       ('Task 3', 'Description for Task 3', 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1,
        '2025-01-10 09:00:00', 'LOW', 'documentation', '2025-01-09 16:30:00');
