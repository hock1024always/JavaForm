package com.homework.topbiz.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homework.topbiz.entity.dto.GroupByUserDTO;
import com.homework.topbiz.entity.dto.UserBindGroupDTO;
import com.homework.topbiz.entity.dto.UserBindPermissionDTO;
import com.homework.topbiz.entity.dto.UsersInGroupDTO;
import com.homework.topbiz.entity.po.Group;
import com.homework.topbiz.entity.po.GroupAndUser;
import com.homework.topbiz.entity.po.Permission;
import com.homework.topbiz.entity.po.User;
import com.homework.topbiz.entity.vo.PageVO;
import com.homework.topbiz.entity.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.List;


@FeignClient(url = "${zxzy.service.userservice.url}", name = "userservice")
public interface UserClient {

    /**
     * 实现用户名密码登录
     * uuy
     */
    @PostMapping("/user/user/loginwithnameandpassword")
    ResultVO loginWithNameAndPassword(@RequestParam String name, @RequestParam String password);

    /**
     * 实现用户名登录
     * uuy
     */
    @PostMapping("/user/user/loginwithname")
    ResultVO loginWithName(@RequestParam String name);

    /**
     * 注册
     */
    @PostMapping("/user/user/userregister")
    ResultVO userRegister(@RequestParam String email, @RequestParam String username, @RequestParam String password);

    /**
     * 登出
     */
    @PostMapping("/user/user/userlogout")
    ResultVO userLogout(@RequestParam String username);


    @PutMapping("/user/user/update")
    ResultVO updateUser(@RequestBody User user);

    /**注销*/
    @PostMapping("/user/user/cancel")
    ResultVO userDelete(@RequestParam String id);


    /**
     * 用户信息查询
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/user/user/getusersinfo")
    ResultVO<Page<User>> getUsersInfo(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    );

    /////////////////////////////////////////// 权限

    /**
     * 创建权限
     *
     * @param permission
     */
    @PostMapping("/permission/create")
    ResultVO createPermission(@RequestBody Permission permission);


    /**
     * 删除权限
     *
     * @param
     * @return
     */
    @PostMapping("/permission/delete")
    ResultVO deletePermission(@RequestBody List<String> permissionIds);

    /**
     * 获取权限列表
     *
     * @param
     * @return
     */
    @GetMapping("/permission/get")
    ResultVO getPermission();

    /////////////////////////////////////////// 权限与用户
    /**
     * 绑定权限与用户
     *
     * @param userBindPermissionDTO
     * @return
     */
    @RequestMapping("/permission-and-user/bind")
    ResultVO bindPermissionAndUser(@RequestBody List<UserBindPermissionDTO> userBindPermissionDTO) throws LoginException;


    /**
     * 解绑权限与用户
     *
     * @return
     */
    @RequestMapping("/permission-and-user/unbind")
    ResultVO unbindPermissionAndUser(@RequestBody List<String> bindIds);


    /**
     * 根据用户id查询权限与用户关系
     *
     * @param userId
     * @return
     */
    @GetMapping("/permission-and-user/selectByUserId")
    ResultVO selectByUserId(@RequestParam("userId") String userId);


    /// ///////////////////////////////////////////////////// 分组

    @PostMapping("/user/group/batch/create")
    ResultVO<List<Group>> create(@RequestBody List<Group> groupList);

    @DeleteMapping("/user/group/batch/delete")
    ResultVO<List<Group>> delete(@RequestBody List<String> ids);


    @PutMapping("/user/group/batch/update_name")
    ResultVO<List<Group>> updateName(@RequestBody List<Group> groupList);

    @GetMapping("/user/group/page/like_group_name")
    ResultVO<PageVO<Group>> pageLikeGroupName(@RequestParam(value = "groupName", required = false) String groupName,
                                              @RequestParam(value = "institutionId", required = false) String institutionId,
                                              @RequestParam(value = "pageNum", required = false, defaultValue = "1") Long pageNum,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize);

    /// /////////////

    @RequestMapping("/user/group/batch/binding")
    ResultVO bindUsersAndGroups(@RequestBody List<UserBindGroupDTO> userBindGroupDTOList);

    @RequestMapping("/user/group/batch/unbind")
    ResultVO<List<GroupAndUser>> unbindUsersAndGroups(@RequestBody List<GroupAndUser> userBindGroupDTOList);

    @PutMapping("/user/group/batch/relation_name")
    ResultVO<List<GroupAndUser>> deleteUsersAndGroups(@RequestBody List<GroupAndUser> groupAndUserList);


    /**
     * 根据分组分页查看用户
     * 应该查所有用户和组的关系，然后返回给前端，前端根据关系展示用户和组的关系
     *
     * @param
     * @return
     */
    @GetMapping("/user/group/page/by_group")
    ResultVO<UsersInGroupDTO> by_group(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "groupId", required = false) String groupId
    );


    @GetMapping("/user/group/page/by_user")
    ResultVO<GroupByUserDTO> groupByUser(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "userId", required = false) String userId);

}