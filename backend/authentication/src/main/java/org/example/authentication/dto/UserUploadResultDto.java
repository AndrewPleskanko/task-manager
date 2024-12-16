package org.example.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUploadResultDto {
    private int success;
    private int failure;
}
