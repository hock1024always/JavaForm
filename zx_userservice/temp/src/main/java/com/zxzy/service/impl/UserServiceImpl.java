package com.zxzy.service.impl;

import com.zxzy.domain.po.User;
import com.zxzy.mapper.UserMapper;
import com.zxzy.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author uuy
 * @since 2025-04-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
