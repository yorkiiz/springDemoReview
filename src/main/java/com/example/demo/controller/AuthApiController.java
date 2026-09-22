package com.example.demo.controller;


import com.example.demo.secruity.JwtUtil;
import com.example.demo.secruity.TokenBlackListService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthApiController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TokenBlackListService tokenBlackListService;

    public AuthApiController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, TokenBlackListService tokenBlackListService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.tokenBlackListService = tokenBlackListService;
    }

    // 表单POST提交到这个地址，校验账号密码返回token
    @PostMapping("/doLogin")
    public String doLogin(@RequestParam String username, @RequestParam String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    /**
     * 登出接口，需要携带token
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            long remainMs = jwtUtil.getTokenRemainExpireMs(token);
            tokenBlackListService.addBlackList(token, remainMs);
        }
        SecurityContextHolder.clearContext();
        return "登出成功";
    }
}
