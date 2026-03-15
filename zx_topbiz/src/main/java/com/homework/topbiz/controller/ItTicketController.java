package com.homework.topbiz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homework.topbiz.entity.po.ItTicket;
import com.homework.topbiz.entity.vo.ResultVO;
import com.homework.topbiz.service.IItTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * IT工单控制器
 * @author JavaForm
 */
@Slf4j
@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@Tag(name = "IT工单管理", description = "工单创建、分配、处理、关闭等操作")
public class ItTicketController {

    private final IItTicketService ticketService;

    @PostMapping("/create")
    @Operation(summary = "创建工单", description = "用户提交IT工单")
    public ResultVO<ItTicket> createTicket(@RequestBody ItTicket ticket, HttpServletRequest request) {
        String userId = request.getHeader("X-User-Id");
        String username = request.getHeader("X-Username");
        ticket.setApplicantId(userId);
        ticket.setApplicantName(username);
        return ResultVO.success(ticketService.createTicket(ticket));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工单", description = "更新工单信息")
    public ResultVO<ItTicket> updateTicket(@RequestBody ItTicket ticket) {
        return ResultVO.success(ticketService.updateTicket(ticket));
    }

    @PostMapping("/assign")
    @Operation(summary = "分配工单", description = "将工单分配给处理人")
    public ResultVO<ItTicket> assignTicket(
            @Parameter(description = "工单ID") @RequestParam String ticketId,
            @Parameter(description = "处理人ID") @RequestParam String handlerId,
            @Parameter(description = "处理人姓名") @RequestParam String handlerName) {
        return ResultVO.success(ticketService.assignTicket(ticketId, handlerId, handlerName));
    }

    @PostMapping("/handle")
    @Operation(summary = "处理工单", description = "处理人工单处理")
    public ResultVO<ItTicket> handleTicket(
            @Parameter(description = "工单ID") @RequestParam String ticketId,
            @Parameter(description = "解决方案") @RequestParam String solution,
            @Parameter(description = "状态") @RequestParam String status) {
        return ResultVO.success(ticketService.handleTicket(ticketId, solution, status));
    }

    @PostMapping("/close")
    @Operation(summary = "关闭工单", description = "关闭工单")
    public ResultVO<ItTicket> closeTicket(
            @Parameter(description = "工单ID") @RequestParam String ticketId,
            @Parameter(description = "备注") @RequestParam(required = false) String remark) {
        return ResultVO.success(ticketService.closeTicket(ticketId, remark));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询工单", description = "分页查询工单列表")
    public ResultVO<Page<ItTicket>> pageTickets(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "工单类型") @RequestParam(required = false) String ticketType,
            @Parameter(description = "优先级") @RequestParam(required = false) String priority,
            @Parameter(description = "申请人ID") @RequestParam(required = false) String applicantId,
            @Parameter(description = "处理人ID") @RequestParam(required = false) String handlerId,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {
        return ResultVO.success(ticketService.pageTickets(pageNum, pageSize, status, ticketType,
                priority, applicantId, handlerId, keyword));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询工单详情", description = "根据ID查询工单详情")
    public ResultVO<ItTicket> getTicket(@PathVariable String id) {
        return ResultVO.success(ticketService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除工单", description = "删除工单（逻辑删除）")
    public ResultVO<Void> deleteTicket(@PathVariable String id) {
        ticketService.removeById(id);
        return ResultVO.success();
    }
}
