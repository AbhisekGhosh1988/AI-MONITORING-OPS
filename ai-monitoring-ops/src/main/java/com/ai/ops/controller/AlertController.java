package com.ai.ops.controller;

import com.ai.ops.entity.AlertEntity;
import com.ai.ops.model.Alert;
import com.ai.ops.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public List<AlertEntity> alerts() {
        return alertService.getAlerts();
    }
}