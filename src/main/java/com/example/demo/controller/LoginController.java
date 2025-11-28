package com.example.demo.controller;

import com.example.demo.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录接口：发放 Token
 */
@RestController
@Tag(name = "登录模块", description = "用户登录获取 Token")
@RequestMapping("/Login")
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 模拟登录（实际项目中需查询数据库验证用户名密码）
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "输入用户名密码，获取 Token（测试用：admin/123456）")
    public Map<String, Object> login(
            @RequestParam(defaultValue = "admin") String username,
            @RequestParam(defaultValue = "123456") String password
    ) {
        Map<String, Object> result = new HashMap<>();

        // 步骤1：验证用户名密码（实际项目中替换为数据库查询，如 UserMapper.selectByUsername(username)）
        if ("admin".equals(username) && "123456".equals(password)) {
            // 步骤2：验证通过，生成 Token
            String token = jwtUtil.generateToken(username);

            // 步骤3：返回 Token 和过期时间（前端需存储 Token，后续请求携带）
            result.put("code", 200);
            result.put("msg", "登录成功");
            result.put("token", token);
            result.put("expiration", jwtUtil.extractExpiration(token));
            result.put("token2","Bearer "+token);
        } else {
            result.put("code", 401);
            result.put("msg", "用户名或密码错误");
        }

        return result;
    }
}