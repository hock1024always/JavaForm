package com.homework.zx_msgservice;

import com.homework.zx_msgservice.domain.po.Message;
import com.homework.zx_msgservice.mapper.MessageMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZxMsgserviceApplicationTests {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    void contextLoads() {
        Message msg = Message.builder()
                .id("123")
                .carrierId("123")
                .templateId("123")
                .templateFill("123")
                .strategyName("123")
                .receiver("123")
                .sendTime(123L)
                .status("123")
                .build();
        messageMapper.insert(msg);
    }

}
