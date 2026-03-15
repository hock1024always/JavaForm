package com.homework.topbiz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homework.topbiz.entity.po.ItApproval;
import com.homework.topbiz.exception.LogicException;
import com.homework.topbiz.mapper.ItApprovalMapper;
import com.homework.topbiz.service.IItApprovalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 审批流程服务实现
 * @author JavaForm
 */
@Slf4j
@Service
public class IItApprovalServiceImpl extends ServiceImpl<ItApprovalMapper, ItApproval> implements IItApprovalService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItApproval createApproval(ItApproval approval) {
        approval.setStatus("PENDING");
        approval.setCreateTime(LocalDateTime.now());
        approval.setUpdateTime(LocalDateTime.now());
        this.save(approval);
        log.info("创建审批申请成功: {}", approval.getId());
        return approval;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItApproval approve(String approvalId, String opinion) {
        ItApproval approval = this.getById(approvalId);
        if (approval == null) {
            throw new LogicException("审批记录不存在");
        }
        if (!"PENDING".equals(approval.getStatus())) {
            throw new LogicException("审批状态不正确");
        }
        approval.setStatus("APPROVED");
        approval.setOpinion(opinion);
        approval.setApprovalTime(LocalDateTime.now());
        approval.setUpdateTime(LocalDateTime.now());
        this.updateById(approval);
        log.info("审批通过: {}", approvalId);
        return approval;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItApproval reject(String approvalId, String opinion) {
        ItApproval approval = this.getById(approvalId);
        if (approval == null) {
            throw new LogicException("审批记录不存在");
        }
        if (!"PENDING".equals(approval.getStatus())) {
            throw new LogicException("审批状态不正确");
        }
        approval.setStatus("REJECTED");
        approval.setOpinion(opinion);
        approval.setApprovalTime(LocalDateTime.now());
        approval.setUpdateTime(LocalDateTime.now());
        this.updateById(approval);
        log.info("审批拒绝: {}", approvalId);
        return approval;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItApproval cancel(String approvalId) {
        ItApproval approval = this.getById(approvalId);
        if (approval == null) {
            throw new LogicException("审批记录不存在");
        }
        if (!"PENDING".equals(approval.getStatus())) {
            throw new LogicException("只能取消待审批的申请");
        }
        approval.setStatus("CANCELLED");
        approval.setUpdateTime(LocalDateTime.now());
        this.updateById(approval);
        log.info("取消审批申请: {}", approvalId);
        return approval;
    }

    @Override
    public Page<ItApproval> pageApprovals(Integer pageNum, Integer pageSize, String status, String approvalType,
                                           String applicantId, String approverId) {
        LambdaQueryWrapper<ItApproval> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(status), ItApproval::getStatus, status)
                .eq(StringUtils.isNotBlank(approvalType), ItApproval::getApprovalType, approvalType)
                .eq(StringUtils.isNotBlank(applicantId), ItApproval::getApplicantId, applicantId)
                .eq(StringUtils.isNotBlank(approverId), ItApproval::getApproverId, approverId)
                .orderByDesc(ItApproval::getCreateTime);

        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public ItApproval getByTicketId(String ticketId) {
        return this.getOne(new LambdaQueryWrapper<ItApproval>()
                .eq(ItApproval::getTicketId, ticketId)
                .orderByDesc(ItApproval::getCreateTime)
                .last("LIMIT 1"));
    }
}
