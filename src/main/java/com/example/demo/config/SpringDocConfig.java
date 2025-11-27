package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SpringDocConfig {

    /**
     * 对应旧配置的 createRestApi() + apiInfo()：配置文档基本信息、安全模式
     */
    @Bean
    public OpenAPI customOpenAPI() {
        // 1. 配置安全模式（对应旧配置的 securitySchemes()，若不需要Token可删除这部分）
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY) // Token 类型（APIKEY）
                .name("Authorization") // 请求头中 Token 的字段名（如 Authorization: Bearer xxx）
                .in(SecurityScheme.In.HEADER) // Token 放在请求头
                .description("请输入 Token（格式：Bearer + 空格 + Token）");

        // 2. 配置文档基本信息（对应旧配置的 apiInfo()）
        Info info = new Info()
                .title("标题：管理系统_接口文档") // 旧配置的 title
                .description("描述：用于管理集团旗下公司的人员信息,具体包括XXX,XXX模块...") // 旧配置的 description
                .contact(new Contact() // 旧配置的 contact（作者信息，null 可保留）
                        .name(null)
                        .email(null))
                .version("版本号:1"); // 旧配置的 version

        // 3. 构建 OpenAPI（对应旧配置的 Docket）
        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList("Token")) // 启用安全模式（关联上面的 securityScheme）
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Token", securityScheme)); // 注册安全模式
    }
}