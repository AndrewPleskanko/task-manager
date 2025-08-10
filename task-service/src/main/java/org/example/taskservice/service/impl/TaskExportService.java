package org.example.taskservice.service.impl;

import java.util.List;
import java.util.function.Function;

import org.example.fileexport.service.ExportService;
import org.example.taskservice.entity.Task;
import org.example.taskservice.repository.TaskRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskExportService {

    private final ExportService exportService;
    private final TaskRepository taskRepository;

    public byte[] exportTasks(String exportType) {
        List<Task> tasks = taskRepository.findAll();

        List<String> headers = List.of("ID", "Title", "Description", "Status");

        Function<Task, List<Object>> rowMapper = task -> List.of(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus()
        );

        return exportService.exportReport(exportType, tasks, headers, rowMapper);
    }
}

