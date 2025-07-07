package com.zxzy.zx_userservice.controller;


import com.zxzy.zx_userservice.domain.po.Group;
import com.zxzy.zx_userservice.domain.vo.PageVO;
import com.zxzy.zx_userservice.domain.vo.ResultVO;
import com.zxzy.zx_userservice.service.IGroupService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分组
 *  前端控制器
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
@RestController
@RequestMapping("/api/user/group")
@Slf4j
public class GroupController {
    @Autowired
    private  IGroupService groupService;

    @PostMapping("/batch/create")
    public ResultVO<List<Group>> create(@RequestBody List<Group> groupList){
        log.info("创建分组 {}",groupList);
        return ResultVO.success(groupService.createGroup(groupList));
    }

    @DeleteMapping("/batch/delete")
    public ResultVO<List<Group>> delete(@RequestBody List<String> ids){
        log.info("删除分组 {}",ids);
        return ResultVO.success(groupService.deleteGroups(ids));
    }


    @PutMapping("/batch/update_name")
    public ResultVO<List<Group>> updateName(@RequestBody List<Group> groupList){
        log.info("更新分组 {}",groupList);
        return ResultVO.success(groupService.updateGroupName(groupList));
    }

    @GetMapping("/page/like_group_name")
    public ResultVO<PageVO<Group>> pageLikeGroupName(@RequestParam(value = "groupName",required = false) String groupName,
                                                    @RequestParam(value = "institutionId",required = false) String institutionId,
                                                    @RequestParam(value = "pageNum",required = false,defaultValue = "1") Long pageNum,
                                                    @RequestParam(value = "pageSize",required = false,defaultValue = "10") Long pageSize) {
        log.info("分页查询分组 groupName={},institutionId={},pageNum={},pageSize={}", groupName, institutionId, pageNum, pageSize);
        PageVO<Group> pageVO = groupService.pageLikeGroupName(groupName, institutionId, pageNum, pageSize);
        return ResultVO.success(pageVO);
    }
}
