package com.zxzy.zx_userservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zxzy.zx_userservice.domain.po.Group;
import com.zxzy.zx_userservice.domain.vo.PageVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
public interface IGroupService extends IService<Group> {

    List<Group> createGroup(List<Group> groupList);

    List<Group> deleteGroups(List<String> ids);

    List<Group> updateGroupName(List<Group> groupList);

    PageVO<Group> pageLikeGroupName(String groupName, String institutionId, Long pageNum, Long pageSize);
}
