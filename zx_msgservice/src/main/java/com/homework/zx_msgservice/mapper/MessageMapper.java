package com.homework.zx_msgservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homework.zx_msgservice.domain.po.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author uuy
 * @since 2025-04-29
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}
