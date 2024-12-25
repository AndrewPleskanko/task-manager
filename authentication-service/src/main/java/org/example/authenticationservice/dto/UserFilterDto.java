package org.example.authenticationservice.dto;

import lombok.Data;
import org.example.authenticationservice.enums.UserReportType;

@Data
public class UserFilterDto {
    private Long roleId;
    private String username;
    private String email;
    private Integer page;
    private Integer size;
    private UserReportType reportType;
}
