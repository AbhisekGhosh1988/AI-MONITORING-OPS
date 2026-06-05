package com.ai.ops.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiError {

    private LocalDateTime timestamp;

    private String message;

    private String path;
}