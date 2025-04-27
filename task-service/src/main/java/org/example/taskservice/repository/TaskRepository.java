package org.example.taskservice.repository;

import java.util.List;
import java.util.Optional;

import org.example.taskservice.entity.Task;
import org.example.taskservice.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);

    Optional<Task> findByIdAndUserId(Long id, Long userId);

    List<Task> findByUserStoryIdAndStatusIn(Long userStoryId, List<Status> statuses);
}
