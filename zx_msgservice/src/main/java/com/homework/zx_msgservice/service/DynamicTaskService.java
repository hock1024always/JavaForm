package com.homework.zx_msgservice.service;

import com.homework.zx_msgservice.domain.dto.SendTaskDTO;

public interface DynamicTaskService {
    void addSendTask(String taskId, String cron, SendTaskDTO sendTaskDTO);

    void addLimitedTask(String taskId, String cron,SendTaskDTO sendTaskDTO, int maxRuns);
    void removeTask(String taskId);
}
