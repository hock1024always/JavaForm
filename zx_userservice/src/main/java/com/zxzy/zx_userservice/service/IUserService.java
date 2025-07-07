package com.zxzy.zx_userservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zxzy.zx_userservice.domain.po.User;
import com.zxzy.zx_userservice.domain.vo.ResultVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
public interface IUserService extends IService<User> {
    public ResultVO LoginWithNameAndPassword(String name, String password);

    //public ResultVO LoginWithEmailAndVerificationCode(String email);
    public ResultVO checkVerificationCode(String email, String verificationCode);

    public ResultVO LoginWithEmailAndPassword(String email, String password);

    public ResultVO LoginWithPhoneAndVerificationCode(String phone, String VerificationCode);

    public ResultVO UserLogout(String username);

    public String generateVerificationCode(String email);

    public ResultVO checkStatus(String username);

    public ResultVO userRegister(String email, String username, String password);

    ResultVO cancel(String id);

    ResultVO registerWithEmailAndVerificationCode(String email);

    ResultVO checkRegister(String email, String password);
}
