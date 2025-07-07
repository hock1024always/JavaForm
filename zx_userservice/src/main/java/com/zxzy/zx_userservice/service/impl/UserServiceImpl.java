package com.zxzy.zx_userservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxzy.zx_userservice.domain.po.User;
import com.zxzy.zx_userservice.domain.vo.ResultVO;
import com.zxzy.zx_userservice.mapper.UserMapper;
import com.zxzy.zx_userservice.service.IUserService;
import com.zxzy.zx_userservice.utils.JWTUtils;
import net.sf.jsqlparser.statement.select.KSQLWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
/**
 * 新导包
 */
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
/**
 * 新导包
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private JWTUtils jwtUtils;
    /**
     * 实现唯一身份信息登录接口
     */
    @Override
    public ResultVO LoginWithNameAndPassword(String name, String password) {
        User user = userMapper.FindUserByName(name);
        if(user == null)
        {
            return ResultVO.error("用户不存在（或密码错误）");
        }
        else{
            if(user.getPassword().equals(password)) {
                userMapper.AlterUserStatusUpName(name);
                String redisKey = "user:" + name;
                redisTemplate.opsForValue().set(redisKey, user, 300, TimeUnit.MINUTES);
                String ID=user.getId();
                String jwtToken = null;
                Map<String, Serializable> res = null;
                try {
                    // 准备 JWT 的 claims
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("userInfo", user);
                    claims.put("Auth", ID);
                    claims.put("Per", "fam");
                    System.out.println("Claims before generating JWT: " + claims);

                    // 生成 JWT 令牌
                    jwtToken = jwtUtils.generateJwt(claims);

                    // 将 JWT 令牌添加到返回结果中
                    res = new HashMap<>();
                    res.put("userInfo", user);
                    res.put("Auth", ID);
                    res.put("Per", "fam");
                    res.put("jwtToken", jwtToken);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResultVO.error("jwt error");
                }
                return ResultVO.success(res);
            } else {
                return ResultVO.error("密码错误");
            }
        }
    }

    /**
     * 实现邮箱验证码登录接口
     */
    public ResultVO LoginWithEmailAndVerificationCode(String email) {
        String verificationCode = generateVerificationCode(email);
        return ResultVO.success("已生成并发送验证码");
    }

    public ResultVO checkVerificationCode(String email, String verificationCode) {
        User user=userMapper.FindUserByEmail(email);
        if(user == null){
            return ResultVO.error("用户不存在");
        }else{
            if(checkEmail(email,verificationCode)){
                userMapper.AlterUserStatusUpEmail(email);
                String redisKey = "email="+email;
                redisTemplate.opsForValue().set(redisKey, user, 300, TimeUnit.MINUTES);
                String ID=user.getId();
                String jwtToken = null;
                Map<String, Serializable> res = null;
                try {
                    // 准备 JWT 的 claims
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("userInfo", user);
                    claims.put("Auth", ID);
                    claims.put("Per", "fam");
                    System.out.println("Claims before generating JWT: " + claims);

                    // 生成 JWT 令牌
                    jwtToken = jwtUtils.generateJwt(claims);

                    // 将 JWT 令牌添加到返回结果中
                    res = new HashMap<>();
                    res.put("userInfo", user);
                    res.put("Auth", ID);
                    res.put("Per", "fam");
                    res.put("jwtToken", jwtToken);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResultVO.error("jwt error");
                }
                return ResultVO.success(res);
            } else {
                return ResultVO.error("密码错误");
            }
        }
    }

    public boolean checkEmail(String email, String verificationCode) {
        String storedCode = stringRedisTemplate.opsForValue().get(email);
        if(verificationCode.equals(storedCode)){
            return true;
        }else{
            return false;
        }
    }

    /*交给msgservice
    public boolean sendVerificationCode(String email){
        String Verificationcode=generateVerificationCode();
        stringRedisTemplate.opsForValue().set(email,Verificationcode,5, TimeUnit.MINUTES);
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("15637700031@163.com");
        message.setTo(email);
        message.setSubject("邮件验证码为："+Verificationcode);
        message.setText("ok");
        mailSender.send(message);
        return true;
    }*/

    /**
     * 实现手机号验证码登录接口
     */
    //TODO：未实现，要花钱
    public ResultVO LoginWithPhoneAndVerificationCode(String phone, String VerificationCode) {
        return new ResultVO<>();
    }

    /**
     * 实现邮箱密码登录接口
     */
//    public ResultVO LoginWithEmailAndPassword(String email, String password) {
//        User user=userMapper.FindUserByEmail(email);
//        if(user == null){
//            return ResultVO.error("邮箱错误");
//        }else
//        {
//            if(user.getPassword().equals(password)){
//                userMapper.AlterUserStatusUpEmail(email);
//                String redisKey = "email:" + email;
//                redisTemplate.opsForValue().set(redisKey, user, 300, TimeUnit.MINUTES);
//                String ID=user.getId();
//                Map<String, Serializable> res = Map.of("userInfo", user,
//                        "Auth", ID,
//                        "Per", "fam"
//                );
//                return ResultVO.success(res);
//            }else{
//                return ResultVO.error("密码错误");
//            }
//        }
//    }
//    TODO:解决对象无法序列化的问题
    public ResultVO LoginWithEmailAndPassword(String email, String password) {
        User user = userMapper.FindUserByEmail(email);
        if (user == null) {
            return ResultVO.error("邮箱错误");
        } else {
            if (user.getPassword().equals(password)) {
                userMapper.AlterUserStatusUpEmail(email);
                String redisKey = "email:" + email;
                try {
                    redisTemplate.opsForValue().set(redisKey, user, 300, TimeUnit.MINUTES);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResultVO.error("redis 错误");
                }
                String ID = user.getId();
                String jwtToken = null;
                Map<String, Serializable> res = null;
                try {
                    // 准备 JWT 的 claims
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("userInfo", user);
                    claims.put("Auth", ID);
                    claims.put("Per", "fam");
                    System.out.println("Claims before generating JWT: " + claims);

                    // 生成 JWT 令牌
                    jwtToken = jwtUtils.generateJwt(claims);

                    // 将 JWT 令牌添加到返回结果中
                    res = new HashMap<>();
                    res.put("userInfo", user);
                    res.put("Auth", ID);
                    res.put("Per", "fam");
                    res.put("jwtToken", jwtToken);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResultVO.error("jwt error");
                }
                return ResultVO.success(res);
            } else {
                return ResultVO.error("密码错误");
            }
        }
    }

    /**
     实现用户登出接口
     */
    public ResultVO UserLogout(String username) {
        userMapper.Logout(username);
        String redisKey = "user:" + username;
        if (redisTemplate.hasKey(redisKey)) {
            redisTemplate.delete(redisKey);
        }
        return ResultVO.success("退出成功");
    }

    /**
     注销账号接口实现
     */

    @Override
    public ResultVO cancel(String id) {
        User user=userMapper.FindUserById(id);
        if(user==null){
            return ResultVO.error("用户不存在");
        }
        try {
            userMapper.deleteuser(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error("error");
        }
        return ResultVO.success("success");
    }

    /**
     邮箱验证码注册功能实现
     */

    @Override
    public ResultVO registerWithEmailAndVerificationCode(String email) {
        User user = userMapper.FindUserByEmail(email);
        if(user!=null){
            return ResultVO.error("已被注册");
        }
        String verificationCode = generateVerificationCodeForRegister(email);
        if (verificationCode == null) {
            return ResultVO.error("空验证码");
        }
        if(verificationCode!=null){
            return ResultVO.success("已生成验证码");
        }
        return ResultVO.error("error");
    }

    @Override
    public ResultVO checkRegister(String email, String verificationCode) {
        User user = userMapper.FindUserByEmail(email);
        if(user!=null){
            return ResultVO.error("已被注册");
        }
        String storedCode = null;
        try{storedCode = stringRedisTemplate.opsForValue().get(email);
            if(storedCode==null){
                return ResultVO.error("空验证码");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultVO.error("error");
        }
        if(verificationCode.equals(storedCode)){
            return ResultVO.success("验证成功，允许注册");
        }
        return ResultVO.error("error");
    }

    /**
     工具
     */

    /**总注册函数接口*/
    public ResultVO userRegister(String email, String username, String password) {
        if(!checkName(username)){
            return ResultVO.error("用户名重复或不合法");
        }
        if(!checkPassword(password)){
            return ResultVO.error("密码不合规");
        }
        try{
            //原始的id生成
            /**String maxid = getMaxId();
            int num = Integer.parseInt(maxid);
            if(num==999){
                return ResultVO.error("数据库已满");
            }
            num++;
            String id=String.format("%03d",num);*/
            //UUID生成
            String id = UUID.randomUUID().toString();
            userMapper.InsertNameAndPassword(id, email, username, password);
        }catch (Exception e){
            e.printStackTrace();
            return ResultVO.error("error");
        }
        return ResultVO.success("创建成功");
    }


    /**生成验证码并存储在redis（用于登录）*/
    public String generateVerificationCode(String email) {
        User user=userMapper.FindUserByEmail(email);
        if(user == null){
            return null;
        }
        String VerificationCode = "123456";
        if(storeRegister(email, VerificationCode)){
            return VerificationCode;
        }
        //stringRedisTemplate.opsForValue().set(email,VerificationCode,5, TimeUnit.MINUTES);
        return null;
    }

    /**生成验证码并存储在redis（用于注册）*/
    public String generateVerificationCodeForRegister(String email) {
        String VerificationCode = "123456";
        if(storeRegister(email, VerificationCode)){
            return VerificationCode;
        }
        //stringRedisTemplate.opsForValue().set(email,VerificationCode,5, TimeUnit.MINUTES);
        return null;
    }

    /**单纯存储验证码*/
    public boolean storeRegister(String email, String verificationCode) {
        String redisKey = email;
        stringRedisTemplate.opsForValue().set(redisKey, verificationCode, 5, TimeUnit.MINUTES);
        return true;
    }

    /**查看名字是否被使用*/
    public boolean checkName(String username){
        User user=userMapper.FindUserByName(username);
        if(user == null){
            return true;
        }else{
            return false;
        }
    }

    /**检查密码安全性*/
    public boolean checkPassword(String password) {
        if (password.length() < 6) {
            return false;
        } else {
            return true;
        }
    }

    /**检查登陆状态*/
    public ResultVO checkStatus(String username) {
        User user=userMapper.FindUserByName(username);
        if(user==null){
            return ResultVO.error("未查询到用户");
        }
        return ResultVO.success(user.getStatus());
    }

    /**获取最大id号*/
    public String getMaxId(){
        return userMapper.getMaxId();
    }
}