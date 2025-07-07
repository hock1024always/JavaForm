package com.zxzy.zx_userservice.controller;


import com.zxzy.zx_userservice.domain.dto.GroupByUserDTO;
import com.zxzy.zx_userservice.domain.dto.UserBindGroupDTO;
import com.zxzy.zx_userservice.domain.dto.UsersInGroupDTO;
import com.zxzy.zx_userservice.domain.po.GroupAndGroup;
import com.zxzy.zx_userservice.domain.po.GroupAndUser;
import com.zxzy.zx_userservice.domain.vo.ResultVO;
import com.zxzy.zx_userservice.service.IGroupAndUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.List;

/**
 * 分组与用户
 *
 * @author uuy
 * @since 2025-04-08
 */
@RestController
@RequestMapping("/api/user/group")
@Slf4j
public class GroupAndUserController {
    @Autowired
    private IGroupAndUserService groupAndUserService;

    @RequestMapping("/batch/binding")
    public ResultVO bindUsersAndGroups(@RequestBody List<UserBindGroupDTO> userBindGroupDTOList) throws LoginException {
        log.info("userBindGroupDTOList: {}", userBindGroupDTOList);
        List<GroupAndUser> groupAndUsers = groupAndUserService.bindUsersAndGroups(userBindGroupDTOList);
        return ResultVO.success(groupAndUsers);
    }

    @RequestMapping("/batch/unbind")
    public ResultVO<List<GroupAndUser>> unbindUsersAndGroups(@RequestBody List<GroupAndUser> userBindGroupDTOList) throws LoginException {
        log.info("GroupAndUserList: {}", userBindGroupDTOList);
        List<GroupAndUser> groupAndUserList = groupAndUserService.unbindUsersAndGroups(userBindGroupDTOList);
        return ResultVO.success(groupAndUserList);
    }

    @PutMapping("/batch/relation_name")
    public ResultVO<List<GroupAndUser>> deleteUsersAndGroups(@RequestBody List<GroupAndUser> groupAndUserList) throws LoginException {
        log.info("修改名称groupAndUserList: {}", groupAndUserList);
        return ResultVO.success(groupAndUserService.updateRelationName(groupAndUserList));
    }

    /**
     * 根据分组分页查看用户
     * 应该查所有用户和组的关系，然后返回给前端，前端根据关系展示用户和组的关系
     * @param
     * @return
     */
    @GetMapping("/page/by_group")
    public ResultVO<UsersInGroupDTO> by_group(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "groupId", required = false) String groupId
    ) throws LoginException {
        log.info("groupId: {}, page: {}, size: {}", groupId, page, size);
        return ResultVO.success(groupAndUserService.by_groupId(groupId, page, size));
    }


    @GetMapping("/page/by_user")
    public ResultVO<GroupByUserDTO> by_user(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "userId", required = false) String userId
    ) throws LoginException {
        log.info("userId: {}, page: {}, size: {}", userId, page, size);
        return ResultVO.success(groupAndUserService.by_userId(userId, page, size));
    }
}