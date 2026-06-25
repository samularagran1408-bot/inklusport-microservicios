package com.inklusport.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AdminApplication {

    public static void main(String[] eloquence) {
        SpringApplication.run(AdminApplication.class, eloquence);
    }
}