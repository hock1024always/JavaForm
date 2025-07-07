package com.homework.topbiz.controller;

import com.homework.topbiz.api.UserClient;
import com.homework.topbiz.entity.dto.UserBindPermissionDTO;
import com.homework.topbiz.entity.po.Permission;
import com.homework.topbiz.entity.vo.ResultVO;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.List;

/**
 * 权限控制
 */
@RestController
@RequestMapping("/api/permission")
@Slf4j
//@GlobalTransactional

public class PermissionController {


    @Autowired
    private UserClient userClient;

    /////////////////////////////////////////// 权限

    /**
     * 创建权限
     * @param permission
     */
    @PostMapping("/create")
    @RequiresPermissions("admin:permission_control")
    public ResultVO createPermission(@RequestBody Permission permission){
        return userClient.createPermission(permission);
    }


    /**
     * 删除权限
     * @param
     * @return
     */
    @PostMapping("/delete")
    @RequiresPermissions("admin:permission_control")
    public ResultVO deletePermission(@RequestBody List<String> permissionIds){
        return userClient.deletePermission(permissionIds);
    }

    /**
     * 获取权限列表
     * @param
     * @return
     */
    @GetMapping("/get")
    @RequiresPermissions("admin:permission_control")
    public ResultVO getPermission(){
        return userClient.getPermission();
    }

    /////////////////////////////////////////// 权限与用户
    /**
     * 绑定权限与用户
     *
     * @param userBindPermissionDTO
     * @return
     */
    @RequestMapping("/bind-user")
    @RequiresPermissions("admin:permission_control")
    public ResultVO bindPermissionAndUser(@RequestBody List<UserBindPermissionDTO> userBindPermissionDTO) throws LoginException{
        return userClient.bindPermissionAndUser(userBindPermissionDTO);
    }


    /**
     * 解绑权限与用户
     *
     * @return
     */
    @RequestMapping("/unbind-user")
    @RequiresPermissions("admin:permission_control")
    public ResultVO unbindPermissionAndUser(@RequestBody List<String> bindIds){
        return userClient.unbindPermissionAndUser(bindIds);
    }


    /**
     * 根据用户id查询权限与用户关系
     *
     * @param userId
     * @return
     */
    @GetMapping("/selectByUserId")
    @RequiresPermissions("admin:permission_control")
    public ResultVO selectByUserId(@RequestParam("userId") String userId){
        return userClient.selectByUserId(userId);
    }

}