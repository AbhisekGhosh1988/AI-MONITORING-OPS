package com.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class LoadController {

    @GetMapping("/cpu")
    public String cpu() {

        long end =
                System.currentTimeMillis()
                        + 120000;
        System.out.println("Starting CPU load test for 2 minutes...");
        while (System.currentTimeMillis() < end) {
            System.out.println("CPU load test running...");
            Math.sqrt(Math.random());

            System.out.println("CPU load test iteration completed...");
        }

        return "CPU test completed";
    }
    @GetMapping("/memory")
    public String memory() {

        List<byte[]> data =
                new ArrayList<>();

        for (int i = 0; i < 500; i++) {

            data.add(
                    new byte[1024 * 1024]
            );
        }

        return "Memory allocated";
    }
}
