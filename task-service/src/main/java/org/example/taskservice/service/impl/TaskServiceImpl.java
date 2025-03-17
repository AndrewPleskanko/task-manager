package org.example.taskservice.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.example.taskservice.entity.Task;
import org.example.taskservice.enums.Priority;
import org.example.taskservice.repository.TaskRepository;
import org.example.taskservice.service.TaskService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public List<Task> getAllTasks(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Optional<Task> getTaskById(String id, Long userId) {
        return taskRepository.findByIdAndUserId(Long.valueOf(id), userId);
    }

    public Task createTask(Task task, Long userId) {
        task.setUserId(userId);
        if (task.getPriority() == null) {
            task.setPriority(Priority.MEDIUM);
        }

        task.setCreatedAt(new Date());
        task.setUpdatedAt(new Date());
        return taskRepository.save(task);
    }

    public Task updateTask(String id, Task taskDetails, Long userId) {
        Optional<Task> taskOptional = taskRepository.findByIdAndUserId(Long.valueOf(id), userId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setStatus(taskDetails.getStatus());
            task.setUpdatedAt(new Date());
            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Task not found with id " + id + " for user " + userId);
        }
    }

    public void deleteTask(String id, Long userId) {
        Optional<Task> taskOptional = taskRepository.findByIdAndUserId(Long.valueOf(id), userId);
        taskOptional.ifPresent(taskRepository::delete);
    }
}

