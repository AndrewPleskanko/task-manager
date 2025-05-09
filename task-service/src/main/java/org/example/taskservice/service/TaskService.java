package org.example.taskservice.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.taskservice.entity.Task;

public interface TaskService {
    List<Task> getAllTasks();

    Optional<Task> getTaskById(String id);

    Task createTask(Task task);

    Task updateTask(String id, Task taskDetails);

    void deleteTask(String id);

    Map<String, Long> getTaskCountByStatus();

    Map<String, Long> getTaskCountByPriority();

    Map<Long, Long> getTaskCountByUser();

    Map<String, Long> getTaskCountByDay();

    Map<String, Long> getCompletedTaskCountByDay();

    List<Task> getActiveTasksByUserStoryId(Long userStoryId);

    Task updateTaskStatus(String id, String newStatus);
}
