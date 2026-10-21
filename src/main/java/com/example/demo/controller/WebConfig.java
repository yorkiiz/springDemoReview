package com.example.demo.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 本地开发（vite 5173）+ k8s 线上入口（NodePort 30080）
                // 注意：用 allowedOriginPatterns 而不是 allowedOrigins，
                // 因为 allowCredentials(true) 与后面通配端口/域名不能同时用字面量 origin。
                // 以后上了域名 + HTTPS，把域名加到这里，否则浏览器带 Origin 的 POST 会被 403。
                .allowedOriginPatterns("http://localhost:*", "http://47.98.20.61:*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DemoInterceptor())
                .addPathPatterns("/**")       // 拦截所有请求
                .excludePathPatterns("/api/hello", "/"); // 排除不需要拦截的地址
    }
}

