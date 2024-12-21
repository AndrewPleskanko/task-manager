package org.example.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserReportDto {
    private byte[] content;
    private String fileName;
}
