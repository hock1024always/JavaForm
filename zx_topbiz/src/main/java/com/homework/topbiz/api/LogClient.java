package com.homework.topbiz.api;


import com.homework.topbiz.entity.po.HttpLog;
import com.homework.topbiz.entity.vo.PageVO;
import com.homework.topbiz.entity.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@FeignClient(url = "${zxzy.service.logservice.url}", name = "logservice")
public interface LogClient {

    /// ///////////////////////////////////////
    /**
     * 准确查询 http log
     * ip地址查询
     * 特定服务名
     * 日志级别
     * 特定路径
     * @return
     */
    @GetMapping("/log/httplog/page-query")
    ResultVO<PageVO<HttpLog>> queryHttpLog(
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
            @RequestParam(value = "clientIp", required = false) String clientIp,
            @RequestParam(value = "serviceName", required = false) String serviceName,
            @RequestParam(value = "logLevel", required = false) String logLevel,
            @RequestParam(value = "requestUri", required = false) String requestUri
    );

    /**
     * 模糊查询 http log
     * ip地址查询
     * 特定服务名
     * 准确日志级别
     * 模糊路径
     * @return
     */
    @GetMapping("/log/httplog/page-search")
    ResultVO<Object> searchHttpLog(
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
            @RequestParam(value = "clientIp", required = false) String clientIp,
            @RequestParam(value = "serviceName", required = false) String serviceName,
            @RequestParam(value = "logLevel", required = false) String logLevel,
            @RequestParam(value = "requestUri", required = false) String requestUri
    );

/// /////////////////////////////////////////////////

    /**
     * 一段时间的访问数目
     * @return
     */
    @GetMapping("/log/http-statistics/num-visit")
    ResultVO<Map<String, Object>> queryHttpLog(
            @RequestParam(value = "startTime", required = false, defaultValue = "1") @DateTimeFormat(pattern = "yyyy-M-d'T'HH:mm:ss") LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false, defaultValue = "10") @DateTimeFormat(pattern = "yyyy-M-d'T'HH:mm:ss") LocalDateTime endTime
    );

    /**
     * 高频访问ip
     * threshold ip访问的平均时间间隔
     * @return
     */
    @GetMapping("/log/http-statistics/high-frequency-ip")
    ResultVO<Map<String, Object>> highFrequencyIps(
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
            @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-M-d'T'HH:mm:ss")LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-M-d'T'HH:mm:ss") LocalDateTime endTime,
            @RequestParam(value = "threshold", required = false, defaultValue = "10") Double threshold,
            @RequestParam(value = "serviceName", required = false) String serviceName

    );

    /**
     * 访问链查询
     * @return
     */
    @GetMapping("/log/http-statistics/visit-chain")
    ResultVO<List<HttpLog>> visitChain(
            @RequestParam(value = "requestId", required = true) String requestId
    );

}
