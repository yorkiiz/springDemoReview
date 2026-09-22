package com.example.demo.controller;

import com.example.demo.secruity.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        // 返回模板名称，对应 resources/templates/login.html
        return "login";
    }

    @PostMapping("/test")
    public String test() {
        return "认证成功，可以访问受保护接口";
    }


    @GetMapping("/hello-view")
    public String helloView(){
        return "hello-view";
    }


}
