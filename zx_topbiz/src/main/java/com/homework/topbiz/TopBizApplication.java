package com.homework.topbiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TopBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(TopBizApplication.class, args);
    }

}
