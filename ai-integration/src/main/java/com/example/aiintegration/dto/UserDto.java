package com.example.aiintegration.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private RoleDto role;
    private int taskLoad;
}
