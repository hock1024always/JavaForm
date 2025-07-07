package com.homework.topbiz.api;

import com.homework.topbiz.entity.dto.SendDTO;
import com.homework.topbiz.entity.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(url = "${zxzy.service.msgservice.url}", name = "msgservice")
public interface MsgClient {

    /**
     * 即时发送 instant
     * 载体 id 默认为 email(邮件)，模板id 默认为空（无模板），模板填充，接收人
     */
    @PostMapping("/msg/send/instant")
    ResultVO<String> instant(@RequestBody SendDTO sendDTO);

    @PostMapping("/msg/send/loginwithname")
    ResultVO loginWithName(String name);
}
