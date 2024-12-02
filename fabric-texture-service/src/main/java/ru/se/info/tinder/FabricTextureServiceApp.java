package ru.se.info.tinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FabricTextureServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(FabricTextureServiceApp.class, args);
    }

}
