package com.zxzy.zx_userservice.mapper;


import com.github.yulichang.base.MPJBaseMapper;
import com.zxzy.zx_userservice.domain.po.GroupAndGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
@Mapper
public interface GroupAndGroupMapper extends MPJBaseMapper<GroupAndGroup> {

    List<GroupAndGroup> getChild(String parentGroupId);
}
