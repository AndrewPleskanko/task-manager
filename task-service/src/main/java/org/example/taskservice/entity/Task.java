package org.example.taskservice.entity;

import java.io.Serial;
import java.util.Date;

import org.example.taskservice.enums.Priority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.example.taskservice.enums.Status;

@Entity
@Data
@Table(name = "tasks")
public class Task {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "assigned_to")
    private Long assignedTo;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "tags")
    private String tags;

    @Column(name = "completed_at")
    private Date completedAt;
}
