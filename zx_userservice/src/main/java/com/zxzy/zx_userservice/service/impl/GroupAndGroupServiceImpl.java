package com.zxzy.zx_userservice.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxzy.zx_userservice.domain.po.Group;
import com.zxzy.zx_userservice.domain.po.GroupAndGroup;
import com.zxzy.zx_userservice.exception.LogicException;
import com.zxzy.zx_userservice.mapper.GroupAndGroupMapper;
import com.zxzy.zx_userservice.mapper.GroupMapper;
import com.zxzy.zx_userservice.service.IGroupAndGroupService;
import com.zxzy.zx_userservice.utils.SnowflakeIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
@Service
public class GroupAndGroupServiceImpl extends ServiceImpl<GroupAndGroupMapper, GroupAndGroup> implements IGroupAndGroupService {

    @Autowired
    private GroupAndGroupMapper groupAndGroupMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private SnowflakeIdUtil snowflakeIdUtil;

    @Override
    public List<GroupAndGroup> bindGroupAndGroup(List<GroupAndGroup> groupAndGroupList) {
        //是否存在这些组
        Set<String> groupIds = new HashSet<>();
        List<String> groupAndGroupIds = new ArrayList<>();
        for (GroupAndGroup cur : groupAndGroupList) {
            if(cur.getParentGroupId().equals(cur.getChildGroupId())) {
                throw new LogicException("不能将组别绑定到自己");
            }
            groupIds.add(cur.getParentGroupId());
            groupIds.add(cur.getChildGroupId());
            String temId = String.valueOf(snowflakeIdUtil.nextId());
            cur.setId(temId);
            groupAndGroupIds.add(temId);
        }
        List<Group> groups = groupMapper.selectByIds(groupIds);
        if(groups.size() != groupIds.size()){
            throw new LogicException("有不存在的组别");
        }
        groupAndGroupMapper.insert(groupAndGroupList);
        return groupAndGroupMapper.selectByIds(groupAndGroupIds);
    }

    @Override
    public List<GroupAndGroup> unbindGroupAndGroup(List<String> groupAndGroupIdList) {
        List<GroupAndGroup> groupAndGroups = groupAndGroupMapper.selectByIds(groupAndGroupIdList);
        if(groupAndGroups.size()!= groupAndGroupIdList.size()){
            throw new LogicException("有不存在的组别");
        }
        removeBatchByIds(groupAndGroupIdList);
        return groupAndGroups;
    }

    @Override
    public List<GroupAndGroup> updateName(List<GroupAndGroup> groupAndGroupList) {
        Set<String> groupIds = new HashSet<>();
        for (GroupAndGroup cur : groupAndGroupList) {
            groupIds.add(cur.getId());
            if(StrUtil.isBlank(cur.getRelationName())){
                throw new LogicException("修改的名字为空");
            }
        }
        if(groupAndGroupList.size() != groupIds.size()){
            throw new LogicException("有不存在的组别");
        }
        groupAndGroupMapper.updateById(groupAndGroupList);
        List<GroupAndGroup> groups = groupAndGroupMapper.selectByIds(groupIds);
        return groups;
    }

    @Override
    public List<GroupAndGroup> getChild(String parentGroupId) {
        groupAndGroupMapper.getChild(parentGroupId);
        return null;
    }


}
