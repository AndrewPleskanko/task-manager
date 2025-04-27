package org.example.taskservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.example.taskservice.entity.Task;
import org.example.taskservice.enums.Priority;
import org.example.taskservice.enums.Status;
import org.example.taskservice.repository.TaskRepository;
import org.example.taskservice.service.impl.TaskServiceImpl;
import org.example.taskservice.utils.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TaskServiceImplIntegrationTest extends BaseServiceTest {

    @Autowired
    private TaskServiceImpl taskService;

    @Autowired
    private TaskRepository taskRepository;

    private Task task1;
    private Task task2;
    private Long userId = 1L;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setPriority(Priority.HIGH);
        task1.setStatus(Status.TO_DO);
        task1.setUserId(userId);

        task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setPriority(Priority.LOW);
        task2.setStatus(Status.IN_PROGRESS);
        task2.setUserId(userId);

        task1 = taskRepository.save(task1);
        task2 = taskRepository.save(task2);
    }

    @Test
    void getAllTasks_shouldReturnAllTasks() {
        List<Task> tasks = taskService.getAllTasks(userId);
        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().anyMatch(task -> task.getTitle().equals("Task 1")));
        assertTrue(tasks.stream().anyMatch(task -> task.getTitle().equals("Task 2")));
    }

    @Test
    void getTaskById_shouldReturnTask_whenTaskExists() {
        Optional<Task> foundTask = taskService.getTaskById(String.valueOf(task1.getId()), userId);
        assertTrue(foundTask.isPresent());
        assertEquals("Task 1", foundTask.get().getTitle());
    }

    @Test
    void getTaskById_shouldReturnEmptyOptional_whenTaskDoesNotExist() {
        Optional<Task> foundTask = taskService.getTaskById("999", userId);
        assertFalse(foundTask.isPresent());
    }

    @Test
    void createTask_shouldCreateAndReturnTask() {
        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("New Description");

        Task createdTask = taskService.createTask(newTask, userId);

        assertNotNull(createdTask.getId());
        assertEquals("New Task", createdTask.getTitle());
        assertEquals(Priority.MEDIUM, createdTask.getPriority());
        assertNotNull(createdTask.getCreatedAt());
        assertNotNull(createdTask.getUpdatedAt());
    }

    @Test
    void updateTask_shouldUpdateExistingTask() {
        Task updatedTaskDetails = new Task();
        updatedTaskDetails.setTitle("Updated Task");
        updatedTaskDetails.setDescription("Updated Description");
        updatedTaskDetails.setStatus(Status.COMPLETED);

        Task updatedTask = taskService.updateTask(String.valueOf(task1.getId()), updatedTaskDetails, userId);

        assertEquals("Updated Task", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());
        assertEquals(Status.COMPLETED, updatedTask.getStatus());
        assertNotNull(updatedTask.getUpdatedAt());
        assertTrue(updatedTask.getUpdatedAt().after(task1.getUpdatedAt()));
    }

    @Test
    void updateTask_shouldThrowException_whenTaskDoesNotExist() {
        Task updatedTaskDetails = new Task();
        updatedTaskDetails.setTitle("Updated Task");

        assertThrows(RuntimeException.class, () -> taskService.updateTask("999", updatedTaskDetails, userId));
    }

    @Test
    void deleteTask_shouldDeleteTask() {
        taskService.deleteTask(String.valueOf(task1.getId()), userId);
        Optional<Task> deletedTask = taskRepository.findById(task1.getId());
        assertFalse(deletedTask.isPresent());
    }

    @Test
    void createTask_shouldSetDefaultPriority_whenPriorityIsNull() {
        Task newTask = new Task();
        newTask.setTitle("Task with no priority");
        Task createdTask = taskService.createTask(newTask, userId);
        assertEquals(Priority.MEDIUM, createdTask.getPriority());
    }

    @Test
    void createTask_shouldSetCreatedAtAndUpdatedAt() {
        Task newTask = new Task();
        newTask.setTitle("Task with timestamps");
        Task createdTask = taskService.createTask(newTask, userId);
        assertNotNull(createdTask.getCreatedAt());
        assertNotNull(createdTask.getUpdatedAt());
    }
}
