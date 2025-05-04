package com.example.aiintegration.dto;

import lombok.Data;

@Data
public class AssignedTaskDto {
    private String taskId;
    private String estimatedStartDate;
    private String estimatedEndDate;
    private Integer estimatedHours;
}