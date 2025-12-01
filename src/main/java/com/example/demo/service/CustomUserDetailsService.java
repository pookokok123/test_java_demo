package com.example.demo.service;


import generator.domain.sys_user;
import generator.service.sys_userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private sys_userService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 从数据库查询用户
        sys_user user = userService.selectByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("<UNK>用户名不存在或已禁用");
        }

        String getRole = user.getRole();
        String getUsername = user.getUsername();
        String getPassword = user.getPassword();

        // 2. 转换角色为Spring Security的权限（角色需加ROLE_前缀）
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (getRole != null && !getRole.isEmpty()) {
            // 支持多个角色（用逗号分隔，如"ADMIN,USER"）
            Arrays.stream(getRole.split(","))
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                    .forEach(authorities::add);
        }

        // 3. 返回Spring Security的UserDetails对象（包含用户名、加密密码、权限）
        return org.springframework.security.core.userdetails.User
                .withUsername(getUsername)
                .password(getPassword)  // 数据库存的是BCrypt加密后的密码，直接使用
                .authorities(authorities)      // 权限集合（角色）
                .accountExpired(false)         // 账号未过期
                .accountLocked(false)          // 账号未锁定
                .credentialsExpired(false)     // 密码未过期
                .disabled(false)                 // 账号启用
                .build();
    }
}
