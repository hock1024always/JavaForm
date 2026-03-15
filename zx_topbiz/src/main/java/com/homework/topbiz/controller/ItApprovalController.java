package com.homework.topbiz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homework.topbiz.entity.po.ItApproval;
import com.homework.topbiz.entity.vo.ResultVO;
import com.homework.topbiz.service.IItApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 审批流程控制器
 * @author JavaForm
 */
@Slf4j
@RestController
@RequestMapping("/approval")
@RequiredArgsConstructor
@Tag(name = "审批流程管理", description = "审批申请、审批通过、审批拒绝等操作")
public class ItApprovalController {

    private final IItApprovalService approvalService;

    @PostMapping("/create")
    @Operation(summary = "创建审批申请", description = "提交审批申请")
    public ResultVO<ItApproval> createApproval(@RequestBody ItApproval approval, HttpServletRequest request) {
        String userId = request.getHeader("X-User-Id");
        String username = request.getHeader("X-Username");
        approval.setApplicantId(userId);
        approval.setApplicantName(username);
        return ResultVO.success(approvalService.createApproval(approval));
    }

    @PostMapping("/approve")
    @Operation(summary = "审批通过", description = "审批人通过申请")
    public ResultVO<ItApproval> approve(
            @Parameter(description = "审批ID") @RequestParam String approvalId,
            @Parameter(description = "审批意见") @RequestParam(required = false) String opinion) {
        return ResultVO.success(approvalService.approve(approvalId, opinion));
    }

    @PostMapping("/reject")
    @Operation(summary = "审批拒绝", description = "审批人拒绝申请")
    public ResultVO<ItApproval> reject(
            @Parameter(description = "审批ID") @RequestParam String approvalId,
            @Parameter(description = "拒绝原因") @RequestParam String opinion) {
        return ResultVO.success(approvalService.reject(approvalId, opinion));
    }

    @PostMapping("/cancel")
    @Operation(summary = "取消申请", description = "申请人取消申请")
    public ResultVO<ItApproval> cancel(
            @Parameter(description = "审批ID") @RequestParam String approvalId) {
        return ResultVO.success(approvalService.cancel(approvalId));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询审批", description = "分页查询审批列表")
    public ResultVO<Page<ItApproval>> pageApprovals(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "审批类型") @RequestParam(required = false) String approvalType,
            @Parameter(description = "申请人ID") @RequestParam(required = false) String applicantId,
            @Parameter(description = "审批人ID") @RequestParam(required = false) String approverId) {
        return ResultVO.success(approvalService.pageApprovals(pageNum, pageSize, status, approvalType, applicantId, approverId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询审批详情", description = "根据ID查询审批详情")
    public ResultVO<ItApproval> getApproval(@PathVariable String id) {
        return ResultVO.success(approvalService.getById(id));
    }

    @GetMapping("/ticket/{ticketId}")
    @Operation(summary = "根据工单查询审批", description = "根据工单ID查询关联的审批记录")
    public ResultVO<ItApproval> getByTicketId(@PathVariable String ticketId) {
        return ResultVO.success(approvalService.getByTicketId(ticketId));
    }
}
