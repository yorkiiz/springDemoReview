package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRestController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Spring Boot4 + Web, Java17 works!";
    }
}
