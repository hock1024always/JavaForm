package com.zxzy.zx_userservice.controller;


import com.zxzy.zx_userservice.domain.vo.ResultVO;
import com.zxzy.zx_userservice.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
@RestController
@RequestMapping("/user/user")
@ResponseBody
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    /**
     * 实现用户名密码登录
     * */
    @PostMapping("/loginwithnameandpassword")
    public ResultVO loginWithNameAndPassword(String name, String password) {
        return userService.LoginWithNameAndPassword(name, password);
        /**
         * 返回json格式的数据
         */
    }

    /**
     * 实现邮箱验证码登录
     * */
    @PostMapping("/loginwithemailandVerificationCode")
    public ResultVO loginWithEmailAndVerificationCode(String email) {
        ResultVO result=userService.LoginWithEmailAndVerificationCode(email);
        return result;
    }
    @PostMapping("/checkVerificationCode")
    public ResultVO checkVerificationCode(String email, String verificationCode) {
        return userService.checkVerificationCode(email, verificationCode);
    }
    @PostMapping("/generateVerificationCode")
    public String generateVerificationCode(String email) {
        return userService.generateVerificationCode(email);
    }
    /**
     * 实现邮箱密码登录
     * */
    @PostMapping("/loginwithemailandpassword")
    public ResultVO loginWithEmailAndPassword(String email, String password) {
        return userService.LoginWithEmailAndPassword(email, password);
    }

    /**
     * 实现手机验证码登录
     * */
    public ResultVO loginWithPhoneAndVerificationCode(String phone, String VerificationCode) {
        return userService.LoginWithPhoneAndVerificationCode(phone, VerificationCode);
    }

    /**登出*/
    @PostMapping("/userlogout")
    public ResultVO userLogout(String username) {
        return userService.UserLogout(username);
    }

    /**检查登陆状态*/
    @PostMapping("/checkstatus")
    public ResultVO checkStatus(String username) {
        return userService.checkStatus(username);
    }

    /**注册*/
    @PostMapping("/userregister")
    public ResultVO userRegister(String email, String username, String password) {
        return userService.userRegister(email, username, password);
    }

    /**注销*/
    @PostMapping("/cancel")
    public ResultVO cancel(String id) {
        return userService.cancel(id);
    }

    @PostMapping("/registerwithemailandverificationcode")
    public ResultVO registerWithEmailAndVerificationCode(String email) {
        return userService.registerWithEmailAndVerificationCode(email);
    }

    @PostMapping("/checkregister")
    public ResultVO checkRegister(String email, String verificationCode) {
        return userService.checkRegister(email, verificationCode);
    }
}