package com.example.aiintegration.service;

import java.util.List;
import java.util.Map;

public interface IProjectService {
    List<Map<String, Object>> getUserStoriesForAI(Long projectId);
}
