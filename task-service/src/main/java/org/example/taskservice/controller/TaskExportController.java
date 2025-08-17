package org.example.taskservice.controller;

import java.util.Locale;

import org.example.taskservice.service.impl.TaskExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tasks/export")
@RequiredArgsConstructor
public class TaskExportController {

    private final TaskExportService taskExportService;

    @GetMapping
    public ResponseEntity<byte[]> exportTasksToExcel(@RequestParam(defaultValue = "excel") String format) {
        byte[] fileBytes = taskExportService.exportTasks(format);

        MediaType mediaType;
        String fileName = switch (format.toLowerCase(Locale.ROOT)) {
            case "excel" -> {
                mediaType = MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                yield "tasks.xlsx";
            }
            case "csv" -> {
                mediaType = MediaType.TEXT_PLAIN;
                yield "tasks.csv";
            }
            default -> {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
                yield "tasks.bin";
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(mediaType)
                .body(fileBytes);
    }
}
