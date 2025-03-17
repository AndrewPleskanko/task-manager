package org.example.taskservice.service;

import java.util.List;
import java.util.Optional;

import org.example.taskservice.entity.Task;

public interface TaskService {
    List<Task> getAllTasks(Long userId);

    Optional<Task> getTaskById(String id, Long userId);

    Task createTask(Task task, Long userId);

    Task updateTask(String id, Task taskDetails, Long userId);

    void deleteTask(String id, Long userId);
}
