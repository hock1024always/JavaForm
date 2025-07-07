package com.zxzy.zx_userservice.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxzy.zx_userservice.domain.po.Group;
import com.zxzy.zx_userservice.domain.vo.PageVO;
import com.zxzy.zx_userservice.exception.LogicException;
import com.zxzy.zx_userservice.mapper.GroupMapper;
import com.zxzy.zx_userservice.service.IGroupService;
import com.zxzy.zx_userservice.utils.SnowflakeIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
@Service
@Transactional
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

    @Autowired
    private SnowflakeIdUtil snowflakeIdUtil;

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public List<Group> createGroup(List<Group> groupList) {

        // 创建一个ArrayList来存储生成的id
        ArrayList<String> ids = new ArrayList<>();
        // 遍历groupList
        for (Group cur : groupList) {
            // 生成一个id
            String id = String.valueOf(snowflakeIdUtil.nextId());
            // 将id添加到ids中
            ids.add(id);
            // 将id赋值给当前group的id
            cur.setId(id);
        }
        // 批量保存groupList
        saveBatch(groupList);

        // 根据ids查询group
        return groupMapper.selectByIds(ids);
    }

    @Override
    public List<Group> deleteGroups(List<String> ids) {
        //获取需要删除的group
        List<Group> groups = groupMapper.selectByIds(ids);
        //删除group
        int i = groupMapper.deleteBatchIds(ids);
        if(i != groups.size()){
            throw new LogicException("ids 中存在不存在的id");
        }
        return groups;
    }

    @Override
    public List<Group> updateGroupName(List<Group> groupList) {
        ArrayList<String> ids = new ArrayList<>();
        for (Group cur : groupList) {
            ids.add(cur.getId());
            if(StrUtil.isEmpty(cur.getGroupName())){
               throw new LogicException("id 为" + cur.getId() + "的groupName不能为空");
            }
            cur.setGroupName(cur.getGroupName());
        }
        groupMapper.updateById(groupList);
        return groupMapper.selectByIds(ids);
    }

    @Override
    public PageVO pageLikeGroupName(String groupName, String institutionId, Long pageNum, Long pageSize) {
        Page page = groupMapper.selectPage(
                new Page(pageNum, pageSize),
                new LambdaQueryWrapper<Group>()
                        .like(StringUtils.hasText(groupName), Group::getGroupName, groupName)
                        .eq(StringUtils.hasText(institutionId), Group::getInstitutionId, institutionId)
        );
        if(page.getRecords().isEmpty()){
            new LogicException("没有查询到数据");
        }
        return PageVO.<Group>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(page.getTotal())
                .dataList(page.getRecords())
                .build();
    }
}
