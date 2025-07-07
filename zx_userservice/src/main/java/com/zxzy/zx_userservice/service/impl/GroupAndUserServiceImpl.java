package com.zxzy.zx_userservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxzy.zx_userservice.domain.dto.GroupByUserDTO;
import com.zxzy.zx_userservice.domain.dto.UserBindGroupDTO;
import com.zxzy.zx_userservice.domain.dto.UsersInGroupDTO;
import com.zxzy.zx_userservice.domain.po.Group;
import com.zxzy.zx_userservice.domain.po.GroupAndUser;
import com.zxzy.zx_userservice.domain.po.User;
import com.zxzy.zx_userservice.domain.vo.PageVO;
import com.zxzy.zx_userservice.mapper.GroupAndUserMapper;
import com.zxzy.zx_userservice.mapper.GroupMapper;
import com.zxzy.zx_userservice.mapper.UserMapper;
import com.zxzy.zx_userservice.service.IGroupAndUserService;
import com.zxzy.zx_userservice.utils.SnowflakeIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.LoginException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
@Service
@Transactional
public class GroupAndUserServiceImpl extends ServiceImpl<GroupAndUserMapper, GroupAndUser> implements IGroupAndUserService {

    @Autowired
    private GroupAndUserMapper groupAndUserMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private SnowflakeIdUtil snowflakeIdUtil;

    @Override
    public List<GroupAndUser> bindUsersAndGroups(List<UserBindGroupDTO> userBindGroupDTOList) throws LoginException {
        // 检验参数
        Set<String> userIds = new HashSet<>();
        Set<String> groupIds = new HashSet<>();
        for (UserBindGroupDTO cur : userBindGroupDTOList) {
            if (StrUtil.isBlank(cur.getUserId()) || StrUtil.isBlank(cur.getGroupId())) {
                throw new LoginException("用户id或者组id为null");
            }
            userIds.add(cur.getUserId());
            groupIds.add(cur.getGroupId());
        }
        // 检查用户是否存在
        List<User> userList = userMapper.selectBatchIds(userIds);
        if (userList.size() != userIds.size()) {
            throw new LoginException("用户不存在");
        }
        // 检查组是否存在
        List<Group> groupList = groupMapper.selectBatchIds(groupIds);
        if (groupList.size() != groupIds.size()) {
            throw new LoginException("组不存在");
        }
        //是否重复绑定(数据库加unqiue约束)

        // 绑定用户和组
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<GroupAndUser> dataList = new ArrayList<>();
        for (UserBindGroupDTO cur : userBindGroupDTOList) {
            GroupAndUser groupAndUser = new GroupAndUser();
            groupAndUser.setId(String.valueOf(snowflakeIdUtil.nextId()));
            groupAndUser.setUserId(cur.getUserId());
            groupAndUser.setGroupId(cur.getGroupId());
            groupAndUser.setRelationName(cur.getRelationName());
            dataList.add(groupAndUser);
            ids.add(groupAndUser.getId());
        }
        groupAndUserMapper.insert(dataList);
        List<GroupAndUser> groupAndUsers = groupAndUserMapper.selectBatchIds(ids);
        return groupAndUsers;
    }

    @Override
    public List<GroupAndUser> unbindUsersAndGroups(List<GroupAndUser> userBindGroupDTOList) throws LoginException {
//        // 解绑用户和组
//        //检查参数
//        List<String> idsOfCondition = new ArrayList<>();
//        for (UserBindGroupDTO cur : userBindGroupDTOList) {
//            idsOfCondition.add("(`" + cur.getUserId() + "`,`" + cur.getGroupId() + "`)");
//        }
//        List<GroupAndUser> ids = groupAndUserMapper.selectList(
//                new QueryWrapper<GroupAndUser>()
//                        .select("id")
//                        .in("(user_id, group_id)", idsOfCondition)
//        );
//        if (ids.size() != userBindGroupDTOList.size()) {
//            throw new LoginException("有不存在的绑定关系，解绑失败");
//        }


        List<GroupAndUser> groupAndUsers = groupAndUserMapper.selectByIds(userBindGroupDTOList.stream().map(GroupAndUser::getId).collect(Collectors.toList()));
        int i = groupAndUserMapper.deleteByIds(userBindGroupDTOList.stream().map(GroupAndUser::getId).collect(Collectors.toList()));
        if (i != groupAndUsers.size()) {
            throw new LoginException("解绑失败，有不存在的绑定关系");
        }
        return groupAndUsers;
    }

    @Override
    public List<GroupAndUser> updateRelationName(List<GroupAndUser> groupAndUserList) throws LoginException {
        //获取ids
        List<String> ids = new ArrayList<>();
        List<GroupAndUser> groupAndUsers = new ArrayList<>();
        for (GroupAndUser cur : groupAndUserList) {
            if (StrUtil.isBlank(cur.getId())) {
                throw new LoginException("id为null");
            }
            if (StrUtil.isBlank(cur.getRelationName())) {
                throw new LoginException("关系名为空");
            }
            GroupAndUser tem = new GroupAndUser();
            tem.setId(cur.getId());
            tem.setRelationName(cur.getRelationName());
            groupAndUsers.add(tem);
            ids.add(cur.getId());
        }
        if (ids.size() != groupAndUserList.size()) {
            throw new LoginException("有不存在的绑定关系，更新失败");
        }
        groupAndUserMapper.updateById(groupAndUsers);
        return groupAndUserMapper.selectBatchIds(ids);
    }

    @Override
    public UsersInGroupDTO by_groupId(String groupId, Integer page, Integer size) throws LoginException {

        // 构建查询条件
        Group group = groupMapper.selectById(groupId);
        if (group == null) {
            throw new LoginException("组不存在");
        }
        List<GroupAndUser> groupAndUserList = groupAndUserMapper.selectList(new LambdaQueryWrapper<GroupAndUser>()
                .eq(GroupAndUser::getGroupId, groupId)
        );
        List<String> userIds = new ArrayList<>();
        for (GroupAndUser cur : groupAndUserList) {
            userIds.add(cur.getUserId());
        }
        Page page1 = userMapper.selectPage(new Page(page, size), new LambdaQueryWrapper<User>()
                .in(User::getId, userIds));
        //封装
        UsersInGroupDTO res = BeanUtil.copyProperties(group, UsersInGroupDTO.class);
        res.setRelationName(groupAndUserList.getFirst().getRelationName());
        res.setUserPageVO(
                PageVO.builder()
                        .pageNum(Long.valueOf(page))
                        .pageSize(Long.valueOf(size))
                        .total(page1.getTotal())
                        .dataList(page1.getRecords())
                        .build()
        );
        return res;
    }

    @Override
    public GroupByUserDTO by_userId(String userId, Integer page, Integer size) throws LoginException {
        // User
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new LoginException("用户不存在");
        }
        // And
        List<GroupAndUser> groupAndUserList = groupAndUserMapper.selectList(new LambdaQueryWrapper<GroupAndUser>()
                .eq(GroupAndUser::getUserId, userId)
        );
        Map<String, String> groupIRelationMap = new ConcurrentHashMap<>();
        for (GroupAndUser cur : groupAndUserList) {
            groupIRelationMap.put(cur.getGroupId(), cur.getRelationName());
        }

        // Group
        Page groupPage = groupMapper.selectPage(new Page(page, size), new LambdaQueryWrapper<Group>()
                .in(Group::getId, groupIRelationMap.keySet()));
        List<Group> records = groupPage.getRecords();
        //封装pagevo
        List<List<Object>> groupPageVOList = new ArrayList<>();
        for (Group cur : records) {
            groupPageVOList.add(Arrays.asList(groupIRelationMap.get(cur.getId()), cur));
        }
        GroupByUserDTO groupByUserDTO = BeanUtil.copyProperties(user, GroupByUserDTO.class);
        groupByUserDTO.setPageNum(Long.valueOf(page));
        groupByUserDTO.setPageSize(Long.valueOf(size));
        groupByUserDTO.setTotal(groupPage.getTotal());
        groupByUserDTO.setGroupAndRelationNameMap(groupPageVOList);
        return groupByUserDTO;
    }
}