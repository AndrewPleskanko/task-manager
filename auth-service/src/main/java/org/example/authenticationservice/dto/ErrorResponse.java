package org.example.authenticationservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private List<String> errors;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}