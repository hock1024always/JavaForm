package com.homework.topbiz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * api测试接口
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String test(){
        return "test success";
    }
}
