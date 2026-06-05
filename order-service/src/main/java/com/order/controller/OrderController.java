package com.order.controller;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class OrderController {

    @GetMapping("/orders")
    public String getOrders() {
        return "Order Service Running Successfully";
    }
}