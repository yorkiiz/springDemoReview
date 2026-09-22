package com.example.demo.secruity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // 自定义未认证入口：直接返回HTML页面，不做302重定向，杜绝死循环
    private final AuthenticationEntryPoint customEntryPoint = new AuthenticationEntryPoint() {
        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException authException) throws IOException {
            String acceptHeader = request.getHeader("Accept");
            response.setContentType("text/html;charset=utf-8");

            // 判断：浏览器页面访问（text/html）返回HTML页面
            if (acceptHeader != null && acceptHeader.contains("text/html")) {
                String html = """
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>访问被拒绝</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
body{background:#f7f9fc;}
</style>
</head>
<body class="d-flex align-items-center justify-content-center vh-100">
<div class="text-center">
<h2 class="text-secondary">403 访问被拒绝</h2>
<p id="tipText" class="mt-3">正在跳转……</p>
</div>
<script>
const token = localStorage.getItem("token");
if(token){
    document.getElementById("tipText").innerText = "检测本地登录凭证，跳转到主页";
    setTimeout(()=>{window.location.href="/api/hello-view";},800);
}else{
    document.getElementById("tipText").innerText = "未登录，跳转到登录页";
    setTimeout(()=>{window.location.href="/api/login";},800);
}
</script>
</body>
</html>
""";
                response.getWriter().write(html);
                return;
            }
            // AJAX接口请求，返回JSON 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"未登录或token失效\"}");
        }
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/login",
                                "/api/doLogin",
                                "/api/hello-view"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customEntryPoint)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
