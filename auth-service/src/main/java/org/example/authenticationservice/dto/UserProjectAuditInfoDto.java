package org.example.authenticationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserProjectAuditInfoDto {
    private String projectName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
