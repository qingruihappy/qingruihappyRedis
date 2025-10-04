package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 */
@RestController
public class TestController {
    
    @GetMapping("/test")
    public String test(
            @RequestParam(required = false) String throwFilterException,
            @RequestParam(required = false) String throwInterceptorException) {
        
        System.out.println("########## Controller 处理请求 ##########");
        
        return "请求处理成功！";
    }
}
