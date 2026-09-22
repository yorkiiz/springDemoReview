package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRestController {

    @GetMapping("/api/hello")
    public String hello() {
        // 获取当前登录认证对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 获取登录用户名
        String username = authentication.getName();
        return "欢迎 用户" + username + "，登录成功";
    }
}
