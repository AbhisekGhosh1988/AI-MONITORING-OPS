package com.ai.ops.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alert {

    private LocalDateTime timestamp;

    private String severity;

    private String message;
}