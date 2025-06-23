package com.UserInteraction.UserInteractionMicroService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class UserInteractionApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserInteractionApplication.class, args);
    }
}
