package ru.se.ifmo.tinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TinderTestApplication {

    public static void main(String[] args) {
        SpringApplication.from(TinderTestApplication::main).with(TinderTestApplication.class).run(args);
    }
}
