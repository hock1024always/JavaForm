package com.homework.zx_msgservice.utils;

import com.homework.zx_msgservice.domain.dto.SendTaskDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class LimitedTaskUtil implements Runnable {
    private  String taskId;              // 即 msgId
    private  SendTaskDTO taskDTO;        // 参数对象
    private  int maxRuns;                // 最大执行次数
    private int runCount = 0;                 // 当前已执行次数
    private Runnable cancelCallback;    // 任务执行完成后的回调（用于移除 ScheduledFuture）

    public LimitedTaskUtil(String taskId, SendTaskDTO taskDTO, int maxRuns, Runnable cancelCallback) {
        this.taskId = taskId;
        this.taskDTO = taskDTO;
        this.maxRuns = maxRuns;
        this.cancelCallback = cancelCallback;
    }

    @Override
    public void run() {
        runCount++;
        log.info("【{}】第 {} 次执行任务: DTO={}", taskId, runCount, taskDTO);

        // TODO: 调用你的发送逻辑
        // sendCarrier(taskDTO);

        if (runCount >= maxRuns) {
            log.info("【{}】达到最大执行次数 {}，任务停止", taskId, maxRuns);
            cancelCallback.run();
        }
    }
}
