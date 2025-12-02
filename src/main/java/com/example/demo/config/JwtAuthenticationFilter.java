package com.example.demo.config;

import com.example.demo.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    // 构造器注入（Spring 6.x推荐构造器注入）
    public JwtAuthenticationFilter(JwtUtils JwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = JwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. 从请求头获取Token（格式：Authorization: Bearer xxx）
            String token = parseJwt(request);

            // 2. 验证Token是否有效
            if (token != null) {
                String username = jwtUtils.getUsernameFromToken(token);
                // 3. 从数据库/内存加载用户信息（UserDetailsService）
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 4. 验证Token是否匹配当前用户
                if (jwtUtils.validateToken(token, userDetails)) {
                    // 5. 创建认证对象（包含用户信息和权限），设置到Security上下文
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities() // 关键：必须传入权限
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 6. 标记用户为“已认证”，后续接口无需再验证
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // Token解析失败（过期、签名错误等），不设置认证信息，后续会返回401
            logger.error("无法设置用户认证信息: {}", e);
        }

        // 7. 放行请求（无论认证成功与否，都继续执行后续过滤器）
        filterChain.doFilter(request, response);
    }

    // 解析请求头中的Token（提取Bearer后面的内容）
    private String parseJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // 截取"Bearer "后面的Token字符串
        }
        return null;
    }
}