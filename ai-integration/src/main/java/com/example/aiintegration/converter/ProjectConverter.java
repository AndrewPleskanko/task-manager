package com.example.aiintegration.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.aiintegration.dto.ProjectDto;
import com.example.aiintegration.dto.UserStoryDto;
import com.example.aiintegration.entity.Project;
import com.example.aiintegration.entity.UserStory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProjectConverter {

    public static ProjectDto toDto(Project project) {
        ProjectDto dto = new ProjectDto();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        return dto;
    }

    public static UserStoryDto toDto(UserStory userStory) {
        UserStoryDto dto = new UserStoryDto();
        dto.setId(userStory.getId());
        dto.setTitle(userStory.getTitle());
        dto.setDescription(userStory.getDescription());
        dto.setStatus(userStory.getStatus());
        dto.setStoryPoints(userStory.getStoryPoints());
        dto.setPriority(userStory.getPriority());
        dto.setTags(userStory.getTags());
        dto.setAcceptanceCriteria(userStory.getAcceptanceCriteria());
        return dto;
    }

    public static List<UserStoryDto> toUserStoryDtos(List<UserStory> userStories) {
        return userStories.stream()
                .map(ProjectConverter::toDto)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public Map<String, Object> convertToAIFormat(UserStory userStory, ObjectMapper objectMapper) {
        UserStoryDto userStoryDto = toDto(userStory);
        return objectMapper.convertValue(userStoryDto, Map.class);
    }

}
