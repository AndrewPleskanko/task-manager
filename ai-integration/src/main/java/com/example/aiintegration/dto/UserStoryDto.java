package com.example.aiintegration.dto;

import lombok.Data;

@Data
public class UserStoryDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Integer storyPoints;
    private Integer priority;
    private String tags;
    private String acceptanceCriteria;
}
