package com.ai.ops.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "scaling_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScalingHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt;
    private String action;
    private Integer replicas;
    @Column(length = 2000)
    private String reason;
    private Integer confidence;
    @Column(length = 4000)
    private String aiExplanation;
}