package com.example.aiintegration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.aiintegration.dto.UserStoryPublicDto;
import com.example.aiintegration.entity.UserStory;

@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
    List<UserStoryPublicDto> findPublicByProjectId(Long projectId);

    List<UserStory> findByProjectId(Long projectId);
}
