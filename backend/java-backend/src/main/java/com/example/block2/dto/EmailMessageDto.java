package com.example.block2.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmailMessageDto {

    private String id;

    private String subject;

    private String content;

    private String recipientEmail;

    private Boolean status;

    private String errorMessage;

    private LocalDate lastAttemptTime;

    private int attemptCount;
}
