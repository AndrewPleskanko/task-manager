package org.example.taskservice.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.taskservice.entity.Task;

public interface TaskService {
    List<Task> getAllTasks(Long userId);

    Optional<Task> getTaskById(String id, Long userId);

    Task createTask(Task task, Long userId);

    Task updateTask(String id, Task taskDetails, Long userId);

    void deleteTask(String id, Long userId);

    Map<String, Long> getTaskCountByStatus(Long userId);

    Map<String, Long> getTaskCountByPriority(Long userId);

    Map<Long, Long> getTaskCountByUser(Long userId);

    Map<String, Long> getTaskCountByDay(Long userId);

    Map<String, Long> getCompletedTaskCountByDay(Long userId);

    List<Task> getActiveTasksByUserStoryId(Long userStoryId);
}
