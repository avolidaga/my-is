package ru.se.info.tinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableReactiveFeignClients
@EnableWebFlux
public class LocationServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(LocationServiceApp.class, args);
    }

}
