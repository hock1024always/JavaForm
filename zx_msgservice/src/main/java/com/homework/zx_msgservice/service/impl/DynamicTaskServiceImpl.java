package com.homework.zx_msgservice.service.impl;

import com.homework.zx_msgservice.domain.dto.SendTaskDTO;
import com.homework.zx_msgservice.mapper.MessageMapper;
import com.homework.zx_msgservice.service.DynamicTaskService;
import com.homework.zx_msgservice.utils.CarrierUtils;
import com.homework.zx_msgservice.utils.LimitedTaskUtil;
import com.homework.zx_msgservice.utils.TaskUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class DynamicTaskServiceImpl implements DynamicTaskService {

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private CarrierUtils carrierUtils;

    @Autowired
    private MessageMapper messageMapper;

    private final Map<String, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();

    public void addSendTask(String taskId, String cron, SendTaskDTO sendTaskDTO) {
        if (taskMap.containsKey(taskId)) {
            System.out.println("任务已存在：" + taskId);
            return;
        }
        // 创建带参数的任务
        Runnable task = new TaskUtil(taskId, sendTaskDTO, carrierUtils,messageMapper);
        ScheduledFuture<?> future = taskScheduler.schedule(task, new CronTrigger(cron));
        taskMap.put(taskId, future);
    }

    @Override
    public void addLimitedTask(String taskId, String cron, SendTaskDTO sendTaskDTO, int maxRuns) {
        if (taskMap.containsKey(taskId)) {
            System.out.println("任务已存在：" + taskId);
            return;
        }
        // 设置取消逻辑
        Runnable cancelCallback = () -> removeTask(taskId);
        // 创建带参数的任务
        Runnable task = new TaskUtil(taskId, sendTaskDTO, carrierUtils, messageMapper, maxRuns,0, cancelCallback);
        CronTrigger trigger = new CronTrigger(cron);
        ScheduledFuture<?> future = taskScheduler.schedule(task, trigger);
        taskMap.put(taskId, future);
    }


    public void removeTask(String taskId) {
        ScheduledFuture<?> future = taskMap.remove(taskId);
        if (future != null) {
            future.cancel(true);
            //System.out.println("任务已取消：" + taskId);
        }
    }
}
