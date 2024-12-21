package org.example.taskservice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.example.taskservice.entity.Task;
import org.example.taskservice.repository.TaskRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        task.setCreatedAt(new Date());
        task.setUpdatedAt(new Date());
        return taskRepository.save(task);
    }

    public Task updateTask(String id, Task taskDetails) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setStatus(taskDetails.getStatus());
            task.setUpdatedAt(new Date());
            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Task not found with id " + id);
        }
    }

    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }
}

