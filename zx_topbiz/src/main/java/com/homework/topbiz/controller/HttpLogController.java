package com.homework.topbiz.controller;

import com.homework.topbiz.api.LogClient;
import com.homework.topbiz.entity.po.HttpLog;
import com.homework.topbiz.entity.vo.PageVO;
import com.homework.topbiz.entity.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * HTTP日志
 */
@RestController
@RequestMapping("/api/httplog")
@Slf4j
//@GlobalTransactional
public class HttpLogController {

    @Autowired
    private LogClient logClient;


    /// ///////////////////////////////////////
    /**
     * 准确查询 http log
     * ip地址查询
     * 特定服务名
     * 日志级别
     * 特定路径
     * @return
     */
    @GetMapping("/httplog/page-query")
    @RequiresPermissions("admin:httplog_control")
    ResultVO<PageVO<HttpLog>> queryHttpLog(
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
            @RequestParam(value = "clientIp", required = false) String clientIp,
            @RequestParam(value = "serviceName", required = false) String serviceName,
            @RequestParam(value = "logLevel", required = false) String logLevel,
            @RequestParam(value = "requestUri", required = false) String requestUri
    ){
        return logClient.queryHttpLog(page, pageSize, clientIp, serviceName, logLevel, requestUri);
    }

    /**
     * 模糊查询 http log
     * ip地址查询
     * 特定服务名
     * 准确日志级别
     * 模糊路径
     * @return
     */
    @GetMapping("/httplog/page-search")
    @RequiresPermissions("admin:httplog_control")
    ResultVO<Object> searchHttpLog(
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
            @RequestParam(value = "clientIp", required = false) String clientIp,
            @RequestParam(value = "serviceName", required = false) String serviceName,
            @RequestParam(value = "logLevel", required = false) String logLevel,
            @RequestParam(value = "requestUri", required = false) String requestUri
    ){
        return logClient.searchHttpLog(page, pageSize, clientIp, serviceName, logLevel, requestUri);
    }

/// /////////////////////////////////////////////////

    /**
     * 一段时间的访问数目
     * @return
     */
    @GetMapping("/http-statistics/num-visit")
    @RequiresPermissions("admin:httplog_control")
    ResultVO<Map<String, Object>> queryHttpLog(
            @RequestParam(value = "startTime", required = false, defaultValue = "1") @DateTimeFormat(pattern = "yyyy-M-d'T'HH:mm:ss") LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false, defaultValue = "10") @DateTimeFormat(pattern = "yyyy-M-d'T'HH:mm:ss") LocalDateTime endTime
    ){
        return logClient.queryHttpLog(startTime, endTime);
    }

    /**
     * 高频访问ip
     * threshold ip访问的平均时间间隔
     * @return
     */
    @GetMapping("/http-statistics/high-frequency-ip")
    @RequiresPermissions("admin:httplog_control")
    ResultVO<Map<String, Object>> highFrequencyIps(
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
            @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-M-d'T'HH:mm:ss")LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-M-d'T'HH:mm:ss") LocalDateTime endTime,
            @RequestParam(value = "threshold", required = false, defaultValue = "10") Double threshold,
            @RequestParam(value = "serviceName", required = false) String serviceName

    ){
        return logClient.highFrequencyIps(page, pageSize, startTime, endTime, threshold, serviceName);
    }

    /**
     * 访问链查询
     * @return
     */
    @GetMapping("/http-statistics/visit-chain")
    @RequiresPermissions("admin:httplog_control")
    ResultVO<List<HttpLog>> visitChain(
            @RequestParam(value = "requestId", required = true) String requestId
    ){
        return logClient.visitChain(requestId);
    }

}
