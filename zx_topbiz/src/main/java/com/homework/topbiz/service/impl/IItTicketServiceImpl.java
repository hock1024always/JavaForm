package com.homework.topbiz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homework.topbiz.entity.po.ItTicket;
import com.homework.topbiz.exception.LogicException;
import com.homework.topbiz.mapper.ItTicketMapper;
import com.homework.topbiz.service.IItTicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * IT工单服务实现
 * @author JavaForm
 */
@Slf4j
@Service
public class IItTicketServiceImpl extends ServiceImpl<ItTicketMapper, ItTicket> implements IItTicketService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItTicket createTicket(ItTicket ticket) {
        ticket.setStatus("PENDING");
        ticket.setCreateTime(LocalDateTime.now());
        ticket.setUpdateTime(LocalDateTime.now());
        this.save(ticket);
        log.info("创建工单成功: {}", ticket.getId());
        return ticket;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItTicket updateTicket(ItTicket ticket) {
        ItTicket existing = this.getById(ticket.getId());
        if (existing == null) {
            throw new LogicException("工单不存在");
        }
        ticket.setUpdateTime(LocalDateTime.now());
        this.updateById(ticket);
        log.info("更新工单成功: {}", ticket.getId());
        return this.getById(ticket.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItTicket assignTicket(String ticketId, String handlerId, String handlerName) {
        ItTicket ticket = this.getById(ticketId);
        if (ticket == null) {
            throw new LogicException("工单不存在");
        }
        if (!"PENDING".equals(ticket.getStatus())) {
            throw new LogicException("工单状态不允许分配");
        }
        ticket.setHandlerId(handlerId);
        ticket.setHandlerName(handlerName);
        ticket.setStatus("PROCESSING");
        ticket.setUpdateTime(LocalDateTime.now());
        this.updateById(ticket);
        log.info("分配工单成功: {} -> {}", ticketId, handlerName);
        return ticket;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItTicket handleTicket(String ticketId, String solution, String status) {
        ItTicket ticket = this.getById(ticketId);
        if (ticket == null) {
            throw new LogicException("工单不存在");
        }
        ticket.setSolution(solution);
        ticket.setStatus(status);
        ticket.setUpdateTime(LocalDateTime.now());
        if ("RESOLVED".equals(status)) {
            ticket.setCompletedTime(LocalDateTime.now());
        }
        this.updateById(ticket);
        log.info("处理工单成功: {}", ticketId);
        return ticket;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItTicket closeTicket(String ticketId, String remark) {
        ItTicket ticket = this.getById(ticketId);
        if (ticket == null) {
            throw new LogicException("工单不存在");
        }
        ticket.setStatus("CLOSED");
        ticket.setRemark(remark);
        ticket.setCompletedTime(LocalDateTime.now());
        ticket.setUpdateTime(LocalDateTime.now());
        this.updateById(ticket);
        log.info("关闭工单成功: {}", ticketId);
        return ticket;
    }

    @Override
    public Page<ItTicket> pageTickets(Integer pageNum, Integer pageSize, String status, String ticketType,
                                       String priority, String applicantId, String handlerId, String keyword) {
        LambdaQueryWrapper<ItTicket> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(status), ItTicket::getStatus, status)
                .eq(StringUtils.isNotBlank(ticketType), ItTicket::getTicketType, ticketType)
                .eq(StringUtils.isNotBlank(priority), ItTicket::getPriority, priority)
                .eq(StringUtils.isNotBlank(applicantId), ItTicket::getApplicantId, applicantId)
                .eq(StringUtils.isNotBlank(handlerId), ItTicket::getHandlerId, handlerId)
                .and(StringUtils.isNotBlank(keyword), w -> w
                        .like(ItTicket::getTitle, keyword)
                        .or()
                        .like(ItTicket::getDescription, keyword))
                .orderByDesc(ItTicket::getCreateTime);

        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }
}
