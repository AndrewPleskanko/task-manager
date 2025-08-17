package com.example.aiintegration.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.aiintegration.service.IAiService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AiService implements IAiService {
    private final RedisTemplate<String, String> redisTemplate;

    public String getAiProjectData(Long projectId) {
        String redisKey = "ai:project:" + projectId;
        System.out.println(redisKey);
        return redisTemplate.opsForValue().get(redisKey);
    }
}
