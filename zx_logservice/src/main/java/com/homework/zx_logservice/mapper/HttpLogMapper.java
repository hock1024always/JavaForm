package com.homework.zx_logservice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homework.zx_logservice.domain.po.HttpLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HttpLogMapper extends BaseMapper<HttpLog> {
}
