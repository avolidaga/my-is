package ru.se.ifmo.tinder.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController implements ErrorController {
    @GetMapping({"/", "/login", "/register", "/error"})
    public String route() {
        return "index.html";
    }
}
