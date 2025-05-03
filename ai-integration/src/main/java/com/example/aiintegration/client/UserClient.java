package com.example.aiintegration.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.aiintegration.dto.UserDto;

@FeignClient(name = "auth-service")
public interface UserClient {

    @GetMapping("/api/v1/users/project/{projectId}")
    List<UserDto> getUserByProjectId(@PathVariable Long projectId);
}
