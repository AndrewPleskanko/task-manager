package com.example.aiintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStoryPublicDto {
    private String title;
    private String description;
    private String status;
    private Integer storyPoints;
    private Integer priority;
    private String tags;
    private String acceptanceCriteria;
}