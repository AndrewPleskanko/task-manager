package org.example.taskservice.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.taskservice.entity.Task;
import org.example.taskservice.enums.Priority;
import org.example.taskservice.enums.Status;
import org.example.taskservice.exception.TaskNotFoundException;
import org.example.taskservice.repository.TaskRepository;
import org.example.taskservice.service.TaskService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks() {
        Long userId = getCurrentUserId();
        return taskRepository.findByUserId(userId);
    }

    @Override
    public Optional<Task> getTaskById(String id) {
        Long userId = getCurrentUserId();
        return taskRepository.findByIdAndUserId(Long.valueOf(id), userId);
    }

    @Override
    public Task createTask(Task task) {
        Long userId = getCurrentUserId();
        task.setUserId(userId);
        if (task.getPriority() == null) {
            task.setPriority(Priority.MEDIUM);
        }

        task.setCreatedAt(new Date());
        task.setUpdatedAt(new Date());
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(String id, Task taskDetails) {
        Long userId = getCurrentUserId();
        Optional<Task> taskOptional = taskRepository.findByIdAndUserId(Long.valueOf(id), userId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setStatus(taskDetails.getStatus());
            task.setUpdatedAt(new Date());
            return taskRepository.save(task);
        } else {
            throw new TaskNotFoundException("Task not found with id " + id + " for user " + userId);
        }
    }

    @Override
    public void deleteTask(String id) {
        Long userId = getCurrentUserId();
        Optional<Task> taskOptional = taskRepository.findByIdAndUserId(Long.valueOf(id), userId);
        if (taskOptional.isPresent()) {
            taskRepository.delete(taskOptional.get());
        } else {
            throw new TaskNotFoundException("Task not found with id " + id + " for user " + userId);
        }
    }

    @Override
    public Map<String, Long> getTaskCountByStatus() {
        Long userId = getCurrentUserId();
        return taskRepository.findByUserId(userId).stream()
                .collect(Collectors.groupingBy(task -> task.getStatus().toString(), Collectors.counting()));
    }

    @Override
    public Map<String, Long> getTaskCountByPriority() {
        Long userId = getCurrentUserId();
        return taskRepository.findByUserId(userId).stream()
                .collect(Collectors.groupingBy(task -> task.getPriority().toString(), Collectors.counting()));
    }

    @Override
    public Map<Long, Long> getTaskCountByUser() {
        Long userId = getCurrentUserId();
        return taskRepository.findByUserId(userId).stream()
                .collect(Collectors.groupingBy(Task::getAssignedTo, Collectors.counting()));
    }

    @Override
    public Map<String, Long> getTaskCountByDay() {
        Long userId = getCurrentUserId();
        return taskRepository.findByUserId(userId).stream()
                .collect(Collectors.groupingBy(task -> task.getCreatedAt().toString().substring(0, 10),
                        Collectors.counting()));
    }

    @Override
    public Map<String, Long> getCompletedTaskCountByDay() {
        Long userId = getCurrentUserId();
        return taskRepository.findByUserId(userId).stream()
                .filter(task -> task.getCompletedAt() != null)
                .collect(Collectors.groupingBy(task -> task.getCompletedAt().toString().substring(0, 10),
                        Collectors.counting()));
    }

    @Override
    public List<Task> getActiveTasksByUserStoryId(Long userStoryId) {
        List<Status> activeStatuses = List.of(Status.TO_DO, Status.IN_PROGRESS, Status.BLOCKED);
        return taskRepository.findByUserStoryIdAndStatusIn(userStoryId, activeStatuses);
    }

    @Override
    public Task updateTaskStatus(String id, String newStatus) {
        Long userId = getCurrentUserId();
        Optional<Task> taskOptional = taskRepository.findByIdAndUserId(Long.valueOf(id), userId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();

            Status status;
            try {
                status = Status.valueOf(newStatus.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid status value: " + newStatus);
            }

            task.setStatus(status);
            task.setUpdatedAt(new Date());

            if (status == Status.COMPLETED) {
                task.setCompletedAt(new Date());
            } else {
                task.setCompletedAt(null);
            }

            return taskRepository.save(task);
        } else {
            throw new TaskNotFoundException("Task not found with id " + id + " for user " + userId);
        }
    }

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
