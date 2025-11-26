package com.example.demo;
import generator.domain.new_table;
import generator.service.new_tableService;
import io.swagger.v3.oas.annotations.Operation;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.ComponentScan; // 导入ComponentScan
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@SpringBootApplication
@RestController
@ComponentScan(basePackages = {
		"com.example.demo", // 主项目包（保留默认扫描）
		"generator.service" // Service接口及实现类所在包（必须加！）
})
@MapperScan("generator.mapper")
public class DemoApplication {

//	@Autowired
//	private new_tableService aaa;

	public static void main(String[] args) {
		String bootVersion = SpringBootVersion.getVersion();
		System.out.println("Spring Boot 版本：" + bootVersion);
		SpringApplication.run(DemoApplication.class, args);
	}
	@Operation(summary = "测试test", description = "查询数据并返回问候语")
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
//		 long count= aaa.count();
//		List<new_table> list = aaa.list();
		String str= String.format("<UNK>%s<UNK>", name);
		return String.format(str+"Hello %s!", name);
	}


}