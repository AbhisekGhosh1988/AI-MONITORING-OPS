package com.ai.ops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiMonitoringOpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiMonitoringOpsApplication.class, args);
    }

}
