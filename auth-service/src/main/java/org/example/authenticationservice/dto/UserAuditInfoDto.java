package org.example.authenticationservice.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserAuditInfoDto {
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

