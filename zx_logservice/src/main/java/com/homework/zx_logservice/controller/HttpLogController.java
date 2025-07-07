package com.homework.zx_logservice.controller;


import com.homework.zx_logservice.domain.po.HttpLog;
import com.homework.zx_logservice.domain.vo.PageVO;
import com.homework.zx_logservice.domain.vo.ResultVO;
import com.homework.zx_logservice.service.HttpLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * http log 查询
 */
@RestController
@RequestMapping("/log/httplog")
@Slf4j
public class HttpLogController {


    @Autowired
    private HttpLogService httplogservice;

    /**
     * 准确查询 http log
     * ip地址查询
     * 特定服务名
     * 日志级别
     * 特定路径
     * @return
     */
    @GetMapping("/page-query")
    public ResultVO<PageVO<HttpLog>> queryHttpLog(
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
            @RequestParam(value = "clientIp", required = false) String clientIp,
            @RequestParam(value = "serviceName", required = false) String serviceName,
            @RequestParam(value = "logLevel", required = false) String logLevel,
            @RequestParam(value = "requestUri", required = false) String requestUri
    ) {
        HttpLog httpLog = new HttpLog();
        httpLog.setClientIp(clientIp);
        httpLog.setServiceName(serviceName);
        httpLog.setLogLevel(logLevel);
        httpLog.setRequestUri(requestUri);
        log.info("query http log httpLog: {} page size: {} page num", httpLog, pageSize, page);
        PageVO<HttpLog> httpLogList = httplogservice.queryHttpLog(httpLog, page, pageSize);
        return ResultVO.success(httpLogList);
    }

    /**
     * 模糊查询 http log
     * ip地址查询
     * 特定服务名
     * 准确日志级别
     * 模糊路径
     * @return
     */
    @GetMapping("/page-search")
    public ResultVO<Object> searchHttpLog(
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
            @RequestParam(value = "clientIp", required = false) String clientIp,
            @RequestParam(value = "serviceName", required = false) String serviceName,
            @RequestParam(value = "logLevel", required = false) String logLevel,
            @RequestParam(value = "requestUri", required = false) String requestUri
    ) {
        HttpLog httpLog = new HttpLog();
        httpLog.setClientIp(clientIp);
        httpLog.setServiceName(serviceName);
        httpLog.setLogLevel(logLevel);
        httpLog.setRequestUri(requestUri);
        log.info("search http log httpLog: {} page size: {} page num", httpLog, pageSize, page);
        PageVO<HttpLog> httpLogList = httplogservice.searchHttpLog(httpLog, page, pageSize);
        return ResultVO.success(httpLogList);
    }

}