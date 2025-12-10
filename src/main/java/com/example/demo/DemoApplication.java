package com.example.demo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.example.demo", // 主项目包（保留默认扫描）
		"generator.service" // Service接口及实现类所在包（必须加！）
})
@MapperScan("generator.mapper")
public class DemoApplication {

	public static void main(String[] args) {
		String bootVersion = SpringBootVersion.getVersion();
		System.out.println("Spring Boot 版本：" + bootVersion);
		SpringApplication.run(DemoApplication.class, args);
	}
}