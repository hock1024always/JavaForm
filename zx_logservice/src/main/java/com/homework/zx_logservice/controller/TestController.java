package com.homework.zx_logservice.controller;


import com.homework.zx_logservice.domain.po.TestLog;
import com.homework.zx_logservice.domain.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * api测试接口
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String test() {
        return "hello world";
    }

    @PostMapping("/post")
    public ResultVO<Map<String, Object>> testPost(@RequestBody TestLog testLog) {
        System.out.println(testLog.toString());
        return ResultVO.success(Map.of("name", testLog.getName(), "age", testLog.getAge()));
    }

}
