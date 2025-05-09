package com.example.aiintegration.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserAssignmentDto {
    private String userId;
    private String userName;
    private List<AssignedTaskDto> assignedTasks;
    private String storyTitle;
}
