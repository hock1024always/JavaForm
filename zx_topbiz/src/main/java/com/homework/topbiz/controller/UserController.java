package com.homework.topbiz.controller;


import com.homework.topbiz.api.MsgClient;
import com.homework.topbiz.api.UserClient;
import com.homework.topbiz.entity.dto.SendDTO;
import com.homework.topbiz.entity.dto.UserBindPermissionDTO;
import com.homework.topbiz.entity.po.User;
import com.homework.topbiz.entity.vo.ResultVO;
import com.homework.topbiz.exception.LogicException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
//@GlobalTransactional
public class UserController {

    @Autowired
    private UserClient userClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MsgClient msgClient;

    /**
     * 实现用户名密码登录
     * uuy
     * */
    @PostMapping("/login-with-name-password")
    public ResultVO loginWithNameAndPassword(String name, String password) {
        return userClient.loginWithNameAndPassword(name, password);
    }


    /**
     * 发送验证码
     * @param name
     * @param email
     * @return
     */
    @PostMapping("/send-verify-code")
    public ResultVO sendVerifyCode(String name, String email){
        //生成验证码
        Random random = new Random();
        String verifyCode = String.format("%06d", random.nextInt(999999));
        //保存验证码到redis
        String key = "verify_code:" + name;
        //设置过期时间为5分钟
        redisTemplate.opsForValue().set(key, verifyCode,10 , TimeUnit.MINUTES);
        //发送验证码
        SendDTO sendDTO = SendDTO.builder()
                .templateId("verify_code")
                .fillContent(List.of(verifyCode,"5分钟"))
                .carrierId("0")
                .subject("验证码")
//                .content("验证码：" + verifyCode)
                .receiver(email)
//                .cronExpression("0 0/5 * * * ?")
                .build();
        return msgClient.instant(sendDTO);
    }

    /**
     * Name和验证码登录
     * @param name
     * @param email
     * @param verifyCode
     * @return
     */
    @PostMapping("/login-with-name-verify-code")
    public ResultVO loginWithNameAndVerifyCode(String name, String email,String verifyCode) {
        String vcode = (String) redisTemplate.opsForValue().get("verify_code:" + name);
        if(vcode == null || "".equals(vcode) ||!vcode.equals(verifyCode)){
            throw new LogicException("验证码错误");
        }
        return userClient.loginWithName(name);
    }

    /**
     * 员工用户注册
     * @param email
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/userregister-emp")
    public ResultVO userRegister(String email, String username, String password) throws LoginException {
        ResultVO resultVO = userClient.userRegister(email, username, password);
        //赋予默认权限
        String userId = (String) resultVO.getData();

        List<String> permissionIds = List.of(
                "712194aa389725360fe194bbacd70169", //user:read
                "d15d32ab4fae1252cb9faf6452933ad5" //user:write
        );
        List<UserBindPermissionDTO> bindDTO = new ArrayList();
        for (String permissionId : permissionIds) {
            bindDTO.add(UserBindPermissionDTO.builder().userId(userId).permissionId(permissionId).build());
        }
        userClient.bindPermissionAndUser(bindDTO);

        return resultVO;
    }

    /**
     * admin用户注册
     * @param email
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/userregister-admin")
    public ResultVO userRegisterAdmin(String email, String username, String password) throws LoginException {
        ResultVO resultVO = userClient.userRegister(email, username, password);
        //赋予默认权限
        String userId = (String) resultVO.getData();

        List<String> permissionIds = List.of(
                "712194aa389725360fe194bbacd70169",//user:read
                "d15d32ab4fae1252cb9faf6452933ad5", //user:write
                "269021cc7660ebe661f98dd5cc5b4051", //user:update
                "d8c0603623757db448efecd9b4d800dd", //user:delete
                "5ba7817d8933c7c48db8bcc962ed9116", //admin:permission_control
                "2d06b8114a5ec0a905380daf8e816fe7", //admin:group_control
                "248084f4b469b09de55a7e317f4fc20c", //admin:user_control
                "0f2710c3f6b22fe17c2460af3661de27", //admin:httplog_control
                "30993e65a8f3cb464bd8f8c1d9b40a0b" //admin:development_record_control
        );
        List<UserBindPermissionDTO> bindDTO = new ArrayList();
        for (String permissionId : permissionIds) {
            bindDTO.add(UserBindPermissionDTO.builder().userId(userId).permissionId(permissionId).build());
        }
        userClient.bindPermissionAndUser(bindDTO);
        return resultVO;
    }

    /**登出*/
    @PostMapping("/userlogout")
    @RequiresPermissions("user:read")
    public ResultVO userLogout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //String userId = (String) redisTemplate.opsForValue().get("user_info:" + token);
        log.debug("token:{}", token);
        return userClient.userLogout(token);
    }


    /**
     * 用户信息修改
     * uuy
     * */
    @PutMapping("/update-self")
    @RequiresPermissions("user:read")
    public ResultVO updateUser (@RequestBody User user,HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String userId = (String) redisTemplate.opsForValue().get("user_info:" + token);
        user.setId(userId);
        return userClient.updateUser(user);
    }

    /**
     * 用户信息查询，管理员用
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/getusersinfo")
    @RequiresPermissions("admin:user_control")
    public ResultVO getUsersInfo(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return ResultVO.success(userClient.getUsersInfo(page, size));
    }

    /**
     * 注销
     * */
    @PostMapping("/user/user/cancel")
    @RequiresPermissions("admin:user_control")
    ResultVO userDelete(String id){
        return userClient.userDelete(id);
    }
}