package com.homework.zx_msgservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.homework.zx_msgservice.domain.dto.SendDTO;
import com.homework.zx_msgservice.domain.dto.SendTaskDTO;
import com.homework.zx_msgservice.domain.po.Message;
import com.homework.zx_msgservice.mapper.MessageMapper;
import com.homework.zx_msgservice.service.DynamicTaskService;
import com.homework.zx_msgservice.service.SendService;
import com.homework.zx_msgservice.utils.CarrierUtils;
import com.homework.zx_msgservice.utils.SnowflakeIdUtil;
import com.homework.zx_msgservice.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@Transactional
@Slf4j
public class SendServiceImpl implements SendService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SnowflakeIdUtil snowflakeIdUtil;

    @Autowired
    private DynamicTaskService dynamicTaskService;

    @Autowired
    private CarrierUtils  carrierUtils;

    @Autowired
    private TemplateUtil  templateUtil;

    @Override
    public void instantSend(SendDTO sendDTO) {
        Optional.ofNullable(sendDTO).map(SendDTO::getReceiver).orElseThrow(() -> new RuntimeException("接收人不能为空"));
        //拼接消息
        if(StrUtil.isNotBlank(sendDTO.getTemplateId())){
            String content = templateUtil.fill(sendDTO.getTemplateId(), sendDTO.getFillContent());
            sendDTO.setContent(content);
        }
        //调用载体发送
        carrierUtils.send(sendDTO.getCarrierId(),sendDTO.getReceiver(), sendDTO.getSubject(), sendDTO.getContent());
        log.info("发送邮件成功 receiver:{}, subject:{}, content:{}",  sendDTO.getReceiver(), sendDTO.getSubject(), sendDTO.getContent());

        // 计入数据库
        Message message = Message.builder()
                .id(String.valueOf(snowflakeIdUtil.nextId()))
                .carrierId(sendDTO.getCarrierId())
                .templateId(sendDTO.getTemplateId())
                .templateFill(sendDTO.getFillContent())
                .strategyName("instant")
                .receiver(sendDTO.getReceiver())
                .sendTime(System.currentTimeMillis())
                .status("already sent")
                .content(sendDTO.getContent())
                .subject(sendDTO.getSubject())
                .build();
        messageMapper.insert(message);
    }

    @Override
    public void timing(SendDTO sendDTO) {
        Optional.of(sendDTO).map(SendDTO::getReceiver).orElseThrow(() -> new RuntimeException("接收人不能为空"));
        Optional.of(sendDTO).map(SendDTO::getCronExpression).orElseThrow(() -> new RuntimeException("Cron不能为空"));

        //拼接消息
        if(StrUtil.isNotBlank(sendDTO.getTemplateId())){
            String content = templateUtil.fill(sendDTO.getTemplateId(), sendDTO.getFillContent());
            sendDTO.setContent(content);
        }
        String dbId = String.valueOf(snowflakeIdUtil.nextId());
        //设置定时任务

        SendTaskDTO sendTaskDTO = SendTaskDTO.builder()
                .content(sendDTO.getContent())
                .dbId(dbId)
                .receiver(sendDTO.getReceiver())
                .subject(sendDTO.getSubject())
                .carrierId(sendDTO.getCarrierId())
                .build();
        //taskId 为 数据库表的id
        dynamicTaskService.addSendTask(dbId, sendDTO.getCronExpression(), sendTaskDTO);
        log.info("添加定时任务成功 taskId:{}, cronExpression:{}, receiver:{}, subject:{}, content:{}", dbId, sendDTO.getCronExpression(), sendDTO.getReceiver(), sendDTO.getSubject(), sendDTO.getContent());

        // 计入数据库
        Message message = Message.builder()
                .id(dbId)
                .carrierId(sendDTO.getCarrierId())
                .templateId(sendDTO.getTemplateId())
                .templateFill(sendDTO.getFillContent())
                .strategyName("instant")
                .receiver(sendDTO.getReceiver())
                .sendTime(System.currentTimeMillis())
                .status("to sent")
                .content(sendDTO.getContent())
                .subject(sendDTO.getSubject())
                .build();
        messageMapper.insert(message);
    }

    @Override
    public void periodSend(SendDTO sendDTO) {
        Optional.of(sendDTO).map(SendDTO::getReceiver).orElseThrow(() -> new RuntimeException("接收人不能为空"));
        Optional.of(sendDTO).map(SendDTO::getCronExpression).orElseThrow(() -> new RuntimeException("Cron不能为空"));

        //拼接消息
        if(StrUtil.isNotBlank(sendDTO.getTemplateId())){
            String content = templateUtil.fill(sendDTO.getTemplateId(), sendDTO.getFillContent());
            sendDTO.setContent(content);
        }
        String dbId = String.valueOf(snowflakeIdUtil.nextId());
        //设置定时任务
        SendTaskDTO sendTaskDTO = SendTaskDTO.builder()
                .content(sendDTO.getContent())
                .dbId(dbId)
                .receiver(sendDTO.getReceiver())
                .subject(sendDTO.getSubject())
                .carrierId(sendDTO.getCarrierId())
                .build();
        //taskId 为 数据库表的id
        dynamicTaskService.addLimitedTask(dbId, sendDTO.getCronExpression(), sendTaskDTO,sendDTO.getRunCount());
        log.info("添加定时任务成功 taskId:{}, cronExpression:{}, receiver:{}, subject:{}, content:{}", dbId, sendDTO.getCronExpression(), sendDTO.getReceiver(), sendDTO.getSubject(), sendDTO.getContent());

        // 计入数据库
        Message message = Message.builder()
                .id(dbId)
                .carrierId(sendDTO.getCarrierId())
                .templateId(sendDTO.getTemplateId())
                .templateFill(sendDTO.getFillContent())
                .strategyName("instant")
                .receiver(sendDTO.getReceiver())
                .sendTime(System.currentTimeMillis())
                .status("to sent")
                .content(sendDTO.getContent())
                .subject(sendDTO.getSubject())
                .build();
        messageMapper.insert(message);
    }

}