package com.homework.zx_msgservice.controller;



import com.homework.zx_msgservice.domain.dto.SendDTO;
import com.homework.zx_msgservice.domain.vo.ResultVO;
import com.homework.zx_msgservice.service.SendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件发送
 * @author uuy
 * @since 2025-04-29
 */
@RestController
@RequestMapping("/msg/send")
@Slf4j
public class SendController {

    @Autowired
    private SendService sendService;

    /**
     * 即时发送 instant
     * 载体 id 默认为 email(邮件)，模板id 默认为空（无模板），模板填充，接收人
     */
    @PostMapping("/instant")
    ResultVO<String> instant(@RequestBody SendDTO sendDTO){
        log.info("即时发送 {}", sendDTO);
        sendService.instantSend(sendDTO);
        return ResultVO.success("success");
    }

    /**
     * 定时发送 timing
     * 秒、分钟、小时、日、月、周
     * 需要回调就讲回调接口地址传过来，否则默认不回调
     * 回调返回的数据作为模板填充字段
     * @param sendDTO
     * @return
     */
    @PostMapping("/timing")
    ResultVO<String> timing(@RequestBody SendDTO sendDTO){
        log.info("定时发送 {}", sendDTO);
        sendService.timing(sendDTO);
        return ResultVO.success("success");
    }

    /**
     * 周期发送 period-send
     * @param sendDTO
     * @return
     */
    @PostMapping("/period-send")
    ResultVO<String> periodSend(@RequestBody SendDTO sendDTO){
        log.info("定时发送 {}", sendDTO);
        sendService.periodSend(sendDTO);
        return ResultVO.success("success");
    }

}
