package com.example.aiintegration.service;

import java.util.List;
import java.util.Map;

import com.example.aiintegration.dto.UserDto;

public interface IProjectService {
    List<Map<String, Object>> getUserStoriesForAI(Long projectId);

    List<UserDto> getUsersByProjectId(Long projectId);

    String processDataWithAi(Long projectId);
}
