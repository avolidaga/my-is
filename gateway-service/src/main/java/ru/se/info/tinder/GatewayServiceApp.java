package ru.se.info.tinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class GatewayServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApp.class, args);
    }
}