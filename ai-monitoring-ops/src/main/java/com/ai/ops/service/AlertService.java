package com.ai.ops.service;

import com.ai.ops.entity.AlertEntity;
import com.ai.ops.model.Alert;
import com.ai.ops.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository repository;

    public void createAlert(String severity, String message) {

        repository.save(AlertEntity.builder().
                createdAt(LocalDateTime.now()).severity(severity).message(message).build());

        System.out.println("[" + severity + "] " + message);
    }

    public List<AlertEntity> getAlerts() {
        return repository.findAll();
    }
}