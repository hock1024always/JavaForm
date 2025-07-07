package com.zxzy.zx_userservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zxzy.zx_userservice.domain.po.GroupAndGroup;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
public interface IGroupAndGroupService extends IService<GroupAndGroup> {

    List<GroupAndGroup> bindGroupAndGroup(List<GroupAndGroup> groupAndGroupList);

    List<GroupAndGroup> unbindGroupAndGroup(List<String> groupAndGroupIdList);

    List<GroupAndGroup> updateName(List<GroupAndGroup> groupAndGroupList);

    List<GroupAndGroup> getChild(String parentGroupId);
}
