package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.MemberVO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.secruity.JwtUtil;
import com.example.demo.secruity.TokenBlackListService;
import com.example.demo.user.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthApiController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TokenBlackListService tokenBlackListService;
    private final MemberService memberService;

    public AuthApiController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, TokenBlackListService tokenBlackListService, MemberService memberService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.tokenBlackListService = tokenBlackListService;
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterDTO dto) {
        memberService.register(dto);
        return Result.success("注册成功", null);
    }

    // 表单POST提交到这个地址，校验账号密码返回token
    @PostMapping("/doLogin")
    public Result<String> doLogin(@RequestParam String username, @RequestParam String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails.getUsername());
        return Result.success("登录成功", token);
    }

    /**
     * 登出接口，需要携带token
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            long remainMs = jwtUtil.getTokenRemainExpireMs(token);
            tokenBlackListService.addBlackList(token, remainMs);
        }
        SecurityContextHolder.clearContext();
        return Result.success("登出成功", null);
    }

    @GetMapping("/user/info")
    public Result<MemberVO> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MemberVO vo = memberService.getUserInfo(username);
        return Result.success(vo);
    }
}
