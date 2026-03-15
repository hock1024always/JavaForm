package com.homework.topbiz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.homework.topbiz.entity.po.ItApproval;

/**
 * 审批流程服务接口
 * @author JavaForm
 */
public interface IItApprovalService extends IService<ItApproval> {

    /**
     * 创建审批申请
     */
    ItApproval createApproval(ItApproval approval);

    /**
     * 审批通过
     */
    ItApproval approve(String approvalId, String opinion);

    /**
     * 审批拒绝
     */
    ItApproval reject(String approvalId, String opinion);

    /**
     * 取消申请
     */
    ItApproval cancel(String approvalId);

    /**
     * 分页查询审批
     */
    Page<ItApproval> pageApprovals(Integer pageNum, Integer pageSize, String status, String approvalType,
                                    String applicantId, String approverId);

    /**
     * 根据工单ID查询审批
     */
    ItApproval getByTicketId(String ticketId);
}
