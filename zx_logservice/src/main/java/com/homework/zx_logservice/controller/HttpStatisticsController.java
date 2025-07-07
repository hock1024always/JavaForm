package com.homework.zx_logservice.controller;

import com.homework.zx_logservice.domain.po.HttpLog;
import com.homework.zx_logservice.domain.vo.PageVO;
import com.homework.zx_logservice.domain.vo.ResultVO;
import com.homework.zx_logservice.service.HttpLogService;
import lombok.extern.slf4j.Slf4j;
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
 * http log 统计
 */
@RestController
@RequestMapping("/log/http-statistics")
@Slf4j
public class HttpStatisticsController {


    @Autowired
    private HttpLogService httplogservice;

    /**
     * 一段时间的访问数目
     * @return
     */
    @GetMapping("/num-visit")
    public ResultVO<Map<String, Object>> queryHttpLog(
            @RequestParam(value = "startTime", required = false, defaultValue = "1") @DateTimeFormat(pattern = "yyyy-M-d'T'HH:mm:ss")LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false, defaultValue = "10") @DateTimeFormat(pattern = "yyyy-M-d'T'HH:mm:ss") LocalDateTime endTime
    ) {

        log.info("num-visit startTime:{}, endTime:{}", startTime, endTime);
        Long numVisit = httplogservice.numVisit(startTime, endTime);
        return ResultVO.success(
                Map.of(
                "numVisit", numVisit,
                "startTime", startTime ,
                "endTime", endTime)
        );
    }

    /**
     * 高频访问ip
     * threshold ip访问的平均时间间隔
     * @return
     */
    @GetMapping("/high-frequency-ip")
    public ResultVO<Map<String, Object>> highFrequencyIps(
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
            @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-M-d'T'HH:mm:ss")LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-M-d'T'HH:mm:ss") LocalDateTime endTime,
            @RequestParam(value = "threshold", required = false, defaultValue = "10") Double threshold,
            @RequestParam(value = "serviceName", required = false) String serviceName

    ) {
        log.info("high-frequency-ip page:{}, pageSize:{}, startTime:{}, endTime:{} threshold:{} serviceName:{}", page, pageSize, startTime, endTime, threshold  , serviceName );
        PageVO<String> ips = httplogservice.highFrequencyIp(page, pageSize, startTime, endTime, threshold, serviceName);

        return ResultVO.success(
                Map.of(
                        "pageIps", ips,
                        "startTime", startTime,
                        "endTime", endTime)
        );
    }


    /**
     * 访问链查询
     * @return
     */
    @GetMapping("/visit-chain")
    public ResultVO<List<HttpLog>> visitChain(
            @RequestParam(value = "requestId", required = true) String requestId
            ) {
        log.info("visit-chain requestId:{}", requestId);
        return ResultVO.success(
                httplogservice.visitChain(requestId)
        );


    }

}
