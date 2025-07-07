package com.homework.zx_logservice;

import com.homework.zx_logservice.domain.po.HttpLog;
import com.homework.zx_logservice.mapper.HttpLogMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ZxLogserviceApplicationTests {

    @Autowired
    private HttpLogMapper httpLogMapper;

    @Test
    void contextLoads() {
        List<HttpLog> httpLogs = httpLogMapper.selectList(null);
        httpLogs.forEach(System.out::println);
    }
}
