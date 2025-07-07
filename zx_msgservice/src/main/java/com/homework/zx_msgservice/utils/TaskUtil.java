package com.homework.zx_msgservice.utils;

import com.homework.zx_msgservice.domain.dto.SendTaskDTO;

import com.homework.zx_msgservice.domain.po.Message;
import com.homework.zx_msgservice.mapper.MessageMapper;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;



@Data
@Builder
@Slf4j
public class TaskUtil implements Runnable {

    private String  taskId; // 即msgId
    private SendTaskDTO taskDTO;
    private CarrierUtils carrierUtils;
    private MessageMapper messageMapper;

    private  Integer maxRuns;                // 最大执行次数
    private Integer runCount = 0;                 // 当前已执行次数
    private Runnable cancelCallback;    // 任务执行完成后的回调（用于移除 ScheduledFuture）

    public TaskUtil(String taskId, SendTaskDTO taskDTO, CarrierUtils carrierUtils, MessageMapper messageMapper, Integer maxRuns,Integer runCount, Runnable cancelCallback) {
        this.taskId = taskId;
        this.taskDTO = taskDTO;
        this.carrierUtils = carrierUtils;
        this.messageMapper = messageMapper;
        this.maxRuns = maxRuns;
        this.cancelCallback = cancelCallback;
    }

    public TaskUtil(String taskId, SendTaskDTO taskDTO, CarrierUtils carrierUtils, MessageMapper messageMapper) {
        this.taskId = taskId;
        this.taskDTO = taskDTO;
        this.maxRuns = 1;
        this.carrierUtils = carrierUtils;
        this.messageMapper = messageMapper;
    }

    @Override
    public void run() {
        runCount++;
        //调用载体发送
        carrierUtils.send(taskDTO.getCarrierId(),taskDTO.getReceiver(), taskDTO.getSubject(), taskDTO.getContent());
        log.info("任务 dbId:{} 已发送", taskDTO.getDbId());
        //更新数据库状态
        messageMapper.updateById(
                Message.builder()
                       .id(taskDTO.getDbId())
                       .status("already sent")
                        .build()
        );
        log.info("开始定时任务 {}", taskId);
        if(runCount >= maxRuns){
            log.info("达到最大执行次数 {}，任务停止", maxRuns);
            if( cancelCallback != null) {
                cancelCallback.run();
            }
        }
    }
}
