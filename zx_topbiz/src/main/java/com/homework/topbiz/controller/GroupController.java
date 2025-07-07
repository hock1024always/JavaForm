package com.homework.topbiz.controller;

import com.homework.topbiz.api.UserClient;
import com.homework.topbiz.entity.dto.GroupByUserDTO;
import com.homework.topbiz.entity.dto.UserBindGroupDTO;
import com.homework.topbiz.entity.dto.UsersInGroupDTO;
import com.homework.topbiz.entity.po.Group;
import com.homework.topbiz.entity.po.GroupAndUser;
import com.homework.topbiz.entity.vo.PageVO;
import com.homework.topbiz.entity.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/group")
@Slf4j
//@GlobalTransactional
public class GroupController {
    
    
    @Autowired
    private UserClient userClient;
    /// ///////////////////////////////////////////////////// 分组

    /**
     * 批量创建分组
     * @param groupList
     * @return
     */
    @PostMapping("/batch/create")
    @RequiresPermissions("admin:group_control")
    ResultVO<List<Group>> create(@RequestBody List<Group> groupList){
        return userClient.create(groupList);
    }

    /**
     * 批量删除分组
     * @param ids
     * @return
     */
    @DeleteMapping("/batch/delete")
    @RequiresPermissions("admin:group_control")
    ResultVO<List<Group>> delete(@RequestBody List<String> ids){
        return userClient.delete(ids);
    }

    /**
     * 批量更新分组名称
     * @param groupList
     * @return
     */
    @PutMapping("/batch/update_name")
    @RequiresPermissions("admin:group_control")
    ResultVO<List<Group>> updateName(@RequestBody List<Group> groupList){
        return userClient.updateName(groupList);
    }

    /**
     * 分页模糊查询分组
     * @param groupName
     * @param institutionId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/page/like_group_name")
    @RequiresPermissions("user:read")
    ResultVO<PageVO<Group>> pageLikeGroupName(@RequestParam(value = "groupName", required = false) String groupName,
                                              @RequestParam(value = "institutionId", required = false) String institutionId,
                                              @RequestParam(value = "pageNum", required = false, defaultValue = "1") Long pageNum,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize){
        return userClient.pageLikeGroupName(groupName, institutionId, pageNum, pageSize);
    }

    /// /////////////

    /**
     * 批量绑定用户和分组
     * @param userBindGroupDTOList
     * @return
     */
    @RequestMapping("/batch/binding")
    @RequiresPermissions("admin:group_control")
    ResultVO bindUsersAndGroups(@RequestBody List<UserBindGroupDTO> userBindGroupDTOList){
        return userClient.bindUsersAndGroups(userBindGroupDTOList);
    }

    /**
     * 批量解绑用户和分组
     * @param userBindGroupDTOList
     * @return
     */
    @RequestMapping("/batch/unbind")
    @RequiresPermissions("admin:group_control")
    ResultVO<List<GroupAndUser>> unbindUsersAndGroups(@RequestBody List<GroupAndUser> userBindGroupDTOList){
        return userClient.unbindUsersAndGroups(userBindGroupDTOList);
    }

    /**
     * 批量修改用户和分组关系名称
     * @param groupAndUserList
     * @return
     */
    @PutMapping("/batch/relation_name")
    @RequiresPermissions("admin:group_control")
    ResultVO<List<GroupAndUser>> deleteUsersAndGroups(@RequestBody List<GroupAndUser> groupAndUserList){
        return userClient.deleteUsersAndGroups(groupAndUserList);
    }


    /**
     * 根据分组分页查看用户
     * 应该查所有用户和组的关系，然后返回给前端，前端根据关系展示用户和组的关系
     *
     * @param
     * @return
     */
    @GetMapping("/page/by_group")
    @RequiresPermissions("user:read")
    ResultVO<UsersInGroupDTO> by_group(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "groupId", required = false) String groupId
    ){
        return userClient.by_group(page, size, groupId);
    }


    /**
     * 获取用户所在组
     * @param
     * @return
     */
    @GetMapping("/page/by_user")
    @RequiresPermissions("user:read")
    ResultVO<GroupByUserDTO> groupByUser(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "userId", required = false) String userId){
        return userClient.groupByUser(page, size, userId);
    }
}
