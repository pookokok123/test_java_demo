package com.example.demo.controller;

import generator.domain.new_table;
import generator.service.new_tableService;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
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
public class TestController {
    @Autowired
    private new_tableService aaa;


    @ApiOperation("测试test1")
    @GetMapping("/test")
    public String hello(@RequestParam(value = "name", defaultValue = "test") String name) {
        long count= aaa.count();
        List<new_table> list = aaa.list();
        String str= String.format("<UNK>%s<UNK>", list.get(0).getA1());
        return String.format(str+"Hello %s!", name);
    }

}
