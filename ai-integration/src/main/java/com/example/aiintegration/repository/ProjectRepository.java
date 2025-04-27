package com.example.aiintegration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.aiintegration.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
