package com.zxzy.zx_userservice.controller;


import com.zxzy.zx_userservice.domain.po.GroupAndGroup;
import com.zxzy.zx_userservice.domain.vo.ResultVO;
import com.zxzy.zx_userservice.service.IGroupAndGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分组与分组
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
@RestController
@RequestMapping("/api/user/group-and-group")
@Slf4j
public class GroupAndGroupController {

    @Autowired

    private IGroupAndGroupService groupAndGroupService;

    /**
     * 绑定群组和群组
     *
     * @return
     */
    @PostMapping("/batch/binding")
    public ResultVO<List<GroupAndGroup>> bindGroupAndGroup(@RequestBody List<GroupAndGroup> groupAndGroupList) {
        log.info("bindGroupAndGroup:{}", groupAndGroupList);
        return ResultVO.success(groupAndGroupService.bindGroupAndGroup(groupAndGroupList));
    }

    @PostMapping("/batch/unbind")
    public ResultVO<List<GroupAndGroup>> unbindGroupAndGroup(@RequestBody List<String> groupAndGroupIdList) {
        log.info("unbindGroupAndGroup:{}", groupAndGroupIdList);
        List<GroupAndGroup> groups = groupAndGroupService.unbindGroupAndGroup(groupAndGroupIdList);
        return ResultVO.success(groups);
    }


    @PostMapping("/batch/update_name")
    public ResultVO<List<GroupAndGroup>> updateName(@RequestBody List<GroupAndGroup> groupAndGroupList) {
        log.info("update_name GroupAndGroup:{}", groupAndGroupList);
        List<GroupAndGroup> groups = groupAndGroupService.updateName(groupAndGroupList);
        return ResultVO.success(groups);
    }

    @GetMapping("/child")
    public ResultVO getChild(
            @RequestParam(value = "parentGroupId", required = true) String parentGroupId
    ) {
        log.info("pageGetChild parentGroupId:{}", parentGroupId);
        List<GroupAndGroup> groups = groupAndGroupService.getChild(parentGroupId);
        return ResultVO.success(groups);
    }

}
