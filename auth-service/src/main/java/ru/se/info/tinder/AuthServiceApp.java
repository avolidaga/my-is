package ru.se.info.tinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class AuthServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApp.class, args);
    }

}
