package com.ai.ops.controller;

import com.ai.ops.service.AutoScalingService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scale")
public class ScaleController {

    private final AutoScalingService service;

    public ScaleController(AutoScalingService service) {
        this.service = service;
    }

    @PostMapping("/{count}")
    public String scale(@PathVariable int count) throws Exception {
        service.scale("order-service", count);
        return "scaled";
    }
}