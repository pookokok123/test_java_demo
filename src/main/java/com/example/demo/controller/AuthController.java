package com.example.demo.controller;

import com.example.demo.util.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    // 登录接口：POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
//        // 1. 验证用户名密码（数据库中的用户）
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//        );
//
//        // 2. 设置认证信息
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. 生成JWT Token
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String token = jwtUtils.generateToken(userDetails);

        // 4. 返回结果给前端
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("msg", "登录成功");
        response.put("token", token);
        response.put("username", userDetails.getUsername());
        response.put("roles", userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", "")) // 去除ROLE_前缀
                .collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }

    // 退出接口：POST /api/auth/logout（前端删除Token即可）
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("code", "200");
        response.put("msg", "退出成功");
        return ResponseEntity.ok(response);
    }

    // 登录请求参数DTO
    public static class LoginRequest {
        private String username;
        private String password;

        // getter/setter
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}