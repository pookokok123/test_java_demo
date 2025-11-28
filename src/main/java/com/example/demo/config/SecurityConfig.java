package com.example.demo.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 启用 Spring Security 配置
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    // 密码编码器（Spring Security 6.x 强制要求配置）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 认证管理器（登录时验证用户名密码）
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 核心过滤链：配置接口放行/拦截规则
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭 CSRF（开发环境简化配置，生产环境可根据需求开启）
                .csrf(csrf -> csrf.disable())

                // 配置接口权限规则
                .authorizeHttpRequests(auth -> auth
                        // 1. 放行 Swagger/Knife4j 相关接口（关键！否则文档无法访问）
                        .requestMatchers(
                                "/doc.html",          // Knife4j 首页
                                "/swagger-ui.html",   // 原生 Swagger 首页
                                "/swagger-ui/**",     // Swagger UI 静态资源
                                "/v3/api-docs/**",    // OpenAPI 接口文档数据
                                "/webjars/**"         // 文档依赖的 WebJar 资源s
                        ).permitAll() // 允许匿名访问
                        .requestMatchers("/api/auth/**").permitAll()
                        // 2. 放行测试接口（可选，根据你的业务调整）
                        .requestMatchers("/Test/test").permitAll()
                        // 3. 其他所有接口需要认证（登录后才能访问）
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 6. 添加JWT过滤器（在UsernamePasswordAuthenticationFilter之前执行）
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 启用默认登录页（访问被拦截的接口时自动跳转）
                .formLogin(form -> form.disable())
                // 启用默认退出功能
                .logout(logout -> logout.disable());

        return http.build();
    }
}