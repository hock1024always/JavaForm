package com.homework.zx_logservice.service;


import com.homework.zx_logservice.domain.po.HttpLog;
import com.homework.zx_logservice.domain.vo.PageVO;

import java.time.LocalDateTime;
import java.util.List;

public interface HttpLogService {
    PageVO<HttpLog> queryHttpLog(HttpLog httpLog, Long pageNum, Long pageSize);

    PageVO<HttpLog> searchHttpLog(HttpLog httpLog,Long pageNum,Long pageSize);

    Long numVisit(LocalDateTime startTime, LocalDateTime endTime);

    PageVO<String> highFrequencyIp(Long page, Long pageSize, LocalDateTime startTime, LocalDateTime endTime, Double threshold, String serviceName);

    List<HttpLog> visitChain(String requestId);
}
