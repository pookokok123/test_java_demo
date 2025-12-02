package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT 工具类：生成 Token、验证 Token、解析 Token 信息
 */
@Component
public class JwtUtil
{
    // 签名密钥（必须复杂！建议配置在 application.yml 中，避免硬编码）
    @Value("${jwt.secret:q3t6w9z$C&F)J@NcRfUjXn2r5u8x/A?D*G-KaPdSgVkYp3s6v9y$B&E)H+MbQeThWmZq4t7w!z%C}")
    private String secret;

    // Token 过期时间（这里设为 2 小时，可根据需求调整）
    @Value("${jwt.expiration:7200000}") // 单位：毫秒
    private long expiration;

    /**
     * 生成签名密钥（jjwt 要求密钥长度至少 256 位，上面的 secret 已满足）
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 1. 生成 Token（基于用户名/用户信息）
     * @param userDetails Spring Security 的用户详情（也可直接传用户名，简化版见下方重载方法）
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(); // 可自定义存储额外信息（如用户角色、权限）
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * 重载：简化版生成 Token（直接传用户名，无需 UserDetails）
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * 核心生成逻辑：设置过期时间、签名算法
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // 自定义负载（可选）
                .setSubject(subject) // Token 主题（通常存用户名）
                .setIssuedAt(new Date(System.currentTimeMillis())) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 过期时间
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 签名算法（HS256 对称加密）
                .compact();
    }

    /**
     * 2. 验证 Token 有效性（用户名匹配 + 未过期）
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 重载：仅验证 Token 未过期（无需用户信息）
     */
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 3. 从 Token 中解析用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 4. 从 Token 中解析自定义负载（如角色、用户ID）
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 解析 Token 所有负载
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // 用相同密钥解析
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 检查 Token 是否过期
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 解析 Token 过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}

