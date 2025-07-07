package com.homework.zx_logservice.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homework.zx_logservice.domain.po.HttpLog;
import com.homework.zx_logservice.domain.vo.PageVO;
import com.homework.zx_logservice.mapper.HttpLogMapper;
import com.homework.zx_logservice.service.HttpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class HttpLogServiceImpl implements HttpLogService {

    @Autowired
    private HttpLogMapper httpLogMapper;

    @Override
    public PageVO<HttpLog> queryHttpLog(HttpLog httpLog,Long pageNum,Long pageSize) {


         Page<HttpLog> page = httpLogMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<HttpLog>()
                /*
                ip地址查询
                特定服务名
                日志级别
                特定路径
                 */
                .eq(StrUtil.isNotBlank(httpLog.getClientIp()), HttpLog::getClientIp, httpLog.getClientIp())
                .eq(StrUtil.isNotBlank(httpLog.getServiceName()), HttpLog::getServiceName, httpLog.getServiceName())
                .eq(StrUtil.isNotBlank(httpLog.getLogLevel()), HttpLog::getLogLevel, httpLog.getLogLevel())
                .eq(StrUtil.isNotBlank(httpLog.getRequestUri()), HttpLog::getRequestUri, httpLog.getRequestUri())
                .orderByDesc(HttpLog::getRequestTime)
        );

        return PageVO.<HttpLog>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(page.getTotal())
                .dataList(page.getRecords())
                .build();
    }

    @Override
    public PageVO<HttpLog> searchHttpLog(HttpLog httpLog,Long pageNum,Long pageSize) {
        Page<HttpLog> page = httpLogMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<HttpLog>()
                /*
                准确查询 http log
                ip地址查询
                特定服务名
                日志级别
                特定路径
                 */
                .like(StrUtil.isNotBlank(httpLog.getClientIp()), HttpLog::getClientIp, httpLog.getClientIp())
                .like(StrUtil.isNotBlank(httpLog.getServiceName()), HttpLog::getServiceName, httpLog.getServiceName())
                .eq(StrUtil.isNotBlank(httpLog.getLogLevel()), HttpLog::getLogLevel, httpLog.getLogLevel())
                .like(StrUtil.isNotBlank(httpLog.getRequestUri()), HttpLog::getRequestUri, httpLog.getRequestUri())
                .orderByDesc(HttpLog::getRequestTime)
        );
        return PageVO.<HttpLog>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(page.getTotal())
                .dataList(page.getRecords())
                .build();
    }

    @Override
    public Long numVisit(LocalDateTime startTime, LocalDateTime endTime) {

        long start = startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long end = endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return httpLogMapper.selectCount(new LambdaQueryWrapper<HttpLog>()
                .between(HttpLog::getRequestTime, start, end)
        );

    }

    @Override
    public PageVO<String> highFrequencyIp(Long page, Long pageSize, LocalDateTime startTime, LocalDateTime endTime, Double threshold, String serviceName) {
        long start = startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long end = endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Page<HttpLog> httpLogPage = httpLogMapper.selectPage(new Page<>(page, pageSize), new LambdaQueryWrapper<HttpLog>()
                .eq(StrUtil.isNotBlank(serviceName), HttpLog::getServiceName, serviceName)
                .between(HttpLog::getRequestTime, start, end)
        );


        if(httpLogPage.getTotal() == 0 || httpLogPage.getRecords().isEmpty()){
            return new PageVO<String>();
        }

        // 按ip分组
        Map<String, Long> ipCountMap = httpLogPage.getRecords().stream().collect(Collectors.groupingBy(HttpLog::getClientIp, Collectors.counting()));

        // 按照访问次数降序排序
        List<Map.Entry<String, Long>> sortedList = ipCountMap.entrySet().stream().sorted((a, b) -> b.getValue().compareTo(a.getValue())).collect(Collectors.toList());

        // 计算阈值 过滤
        sortedList.stream().filter(entry -> (double) ((end - start) / 1000) / threshold <= threshold);

        List<String> dataList = sortedList.stream().map(Map.Entry::getKey).collect(Collectors.toList());

        return PageVO.<String>builder()
                .pageNum(page)
                .pageSize(pageSize)
                .dataList(dataList)
                .build();
    }

    @Override
    public List<HttpLog> visitChain(String requestId) {
        return httpLogMapper.selectList(new LambdaQueryWrapper<HttpLog>()
                .eq(HttpLog::getRequestId, requestId)
        );
    }
}
