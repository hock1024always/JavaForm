package com.zxzy.zx_userservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zxzy.zx_userservice.domain.dto.GroupByUserDTO;
import com.zxzy.zx_userservice.domain.dto.UserBindGroupDTO;
import com.zxzy.zx_userservice.domain.dto.UsersInGroupDTO;
import com.zxzy.zx_userservice.domain.po.GroupAndUser;

import javax.security.auth.login.LoginException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
public interface IGroupAndUserService extends IService<GroupAndUser> {

    List<GroupAndUser> bindUsersAndGroups(List<UserBindGroupDTO> userBindGroupDTOList) throws LoginException;

    List<GroupAndUser> unbindUsersAndGroups(List<GroupAndUser> userBindGroupDTOList) throws LoginException;

    List<GroupAndUser> updateRelationName(List<GroupAndUser> groupAndUserList) throws LoginException;

    UsersInGroupDTO by_groupId(String groupId, Integer page, Integer size) throws LoginException;

    GroupByUserDTO by_userId(String userId, Integer page, Integer size) throws LoginException;
}
