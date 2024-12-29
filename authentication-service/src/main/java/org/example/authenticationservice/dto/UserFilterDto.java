package org.example.authenticationservice.dto;

import org.example.authenticationservice.enums.UserReportType;

import lombok.Data;

@Data
public class UserFilterDto {
    private Long roleId;
    private String username;
    private String email;
    private Integer page;
    private Integer size;
    private UserReportType reportType;
}
