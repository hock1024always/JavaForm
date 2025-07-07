package com.zxzy.zx_userservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxzy.zx_userservice.domain.po.Permission;
import com.zxzy.zx_userservice.mapper.PermissionMapper;
import com.zxzy.zx_userservice.service.IPermissionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
