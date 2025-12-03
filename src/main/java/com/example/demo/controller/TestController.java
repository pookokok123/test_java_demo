package com.example.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.util.JwtUtils;
import generator.domain.new_table;
import generator.domain.sys_user;
import generator.service.new_tableService;
import generator.service.sys_userService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
@Tag(name = "用户管理与商品查询接口", description = "包含用户CRUD、商品模糊查询等核心业务接口")
@RequestMapping("/Test")
public class TestController {
    @Autowired
    private new_tableService tableService;

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private sys_userService userService;

    @Operation(summary = "测试test1", description = "查询数据并返回问候语")
    @GetMapping("/test")
    public String hello(@RequestParam(value = "name", defaultValue = "test") String name) {
       long count= tableService.count();//aba

        sys_user aa=new sys_user();
        aa.setUsername(name);
        aa.setPassword("123456");

        sys_user user= userService.selectByUserName(name);

        String token = jwtUtil.getUsernameFromToken("admin");
        List<new_table> list = tableService.list();
        String str= String.format("<UNK>%s<UNK>", list.get(0).getA1());
        return String.format(str+"Hello %s!|"+token, name);
    }

    @Operation(summary = "测试test2")
    @GetMapping("/GetTest")
    public String GetTest(@RequestParam(value = "name", defaultValue = "test") String name)
    {
        return name;
    }

    @GetMapping("/page")
    public IPage<new_table> GetPage(
            @RequestParam(defaultValue = "1") Integer pageNum, // 默认第1页
            @RequestParam(defaultValue = "10") Integer pageSize) { // 默认每页10条
        return tableService.GetPage(pageNum, pageSize);
    }

}
