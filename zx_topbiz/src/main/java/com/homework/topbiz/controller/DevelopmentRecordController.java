package com.homework.topbiz.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homework.topbiz.entity.po.DevelopmentRecord;
import com.homework.topbiz.entity.vo.ResultVO;
import com.homework.topbiz.exception.LogicException;
import com.homework.topbiz.service.IDevelopmentRecordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

/**
 * 开发记录
 * @author uuy
 * @since 2025-06-23
 */
@RestController
@RequestMapping("/development-record")
@Slf4j

public class DevelopmentRecordController {

    @Autowired
    private IDevelopmentRecordService developmentRecordService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 添加开发记录
     * @param developmentRecord
     * @param request
     * @return
     */
    @PostMapping("/add")
    @RequiresPermissions(value = {"user:write","admin:development_record_control"}, logical = Logical.OR)
    public ResultVO addDevelopmentRecord(
            @RequestBody DevelopmentRecord developmentRecord,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String userId = (String) redisTemplate.opsForValue().get("user_info:" + token);
        Optional.of(developmentRecord).map(DevelopmentRecord::getRecordContent).orElseThrow(
                () -> new LogicException("请填写开发记录")
        );
        developmentRecord.setUserId(userId);
        developmentRecord.setCreateBy(userId);
        developmentRecordService.save(developmentRecord);
        return ResultVO.success(developmentRecordService.getById(developmentRecord.getId()));
    }


    /**
     * 更新开发记录
     * @param developmentRecord
     * @param request
     * @return
     */
    @PutMapping("/update")
    @RequiresPermissions(value = {"user:update","admin:development_record_control"}, logical = Logical.OR)
    public ResultVO updateDevelopmentRecord(
            @RequestBody DevelopmentRecord developmentRecord,
            HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        String userId = (String) redisTemplate.opsForValue().get("user_info:" + token);
        Optional.of(developmentRecord).map(DevelopmentRecord::getId).orElseThrow(
                () -> new LogicException("请填写开发记录id"));
        developmentRecord.setUpdateBy(userId);
        //
        if(developmentRecord.getUserId() == null ){
            developmentRecord.setUserId(userId);
        }
        developmentRecordService.updateById(developmentRecord);
        return ResultVO.success(developmentRecordService.getById(developmentRecord.getId()));
    }

    /**
     * 删除开发记录
     * @param ids
     * @param request
     * @return
     */
    @DeleteMapping("/delete")
    @RequiresPermissions(value = {"user:delete","admin:development_record_control"}, logical = Logical.OR)
    public ResultVO deleteDevelopmentRecord(@PathVariable List<String> ids, HttpServletRequest request) {
        List<DevelopmentRecord> developmentRecords = developmentRecordService.listByIds(ids);
        // 删除开发记录
        developmentRecordService.removeByIds(ids);
        return ResultVO.success(developmentRecords);
    }

    /**
     * 管理员查看开发记录
     */
    @GetMapping("/list-admin")
    @RequiresPermissions("admin:development_record_control")
    public ResultVO listDevelopmentRecord(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String institutionId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String recordContent
    ) {
        Page<DevelopmentRecord> page = developmentRecordService.page(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<DevelopmentRecord>()
                        .eq(StringUtils.isNotBlank(institutionId), DevelopmentRecord::getInstitutionId, institutionId)
                        .eq(StringUtils.isNotBlank(status), DevelopmentRecord::getStatus, status)
                        .eq(StringUtils.isNotBlank(userId), DevelopmentRecord::getUserId, userId)
                        .like(StringUtils.isNotBlank(recordContent), DevelopmentRecord::getRecordContent, recordContent)
        );
        return ResultVO.success(page);
    }



    /**
     * 员工查看开发记录
     */
    @GetMapping("/list-emp")
    @RequiresPermissions(value = {"user:read","admin:development_record_control"}, logical = Logical.OR)
    public ResultVO listDevelopmentRecordEmp(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String recordContent,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        String userId = (String) redisTemplate.opsForValue().get("user_info:" + token);

        Page<DevelopmentRecord> page = developmentRecordService.page(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<DevelopmentRecord>()
                        .eq(DevelopmentRecord::getUserId, userId)
                        .eq(StringUtils.isNotBlank(status), DevelopmentRecord::getStatus, status)
                        .like(StringUtils.isNotBlank(recordContent), DevelopmentRecord::getRecordContent, recordContent)
        );
        return ResultVO.success(page);
    }
}
