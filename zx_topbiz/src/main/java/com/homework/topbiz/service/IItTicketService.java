package com.homework.topbiz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.homework.topbiz.entity.po.ItTicket;

/**
 * IT工单服务接口
 * @author JavaForm
 */
public interface IItTicketService extends IService<ItTicket> {

    /**
     * 创建工单
     */
    ItTicket createTicket(ItTicket ticket);

    /**
     * 更新工单
     */
    ItTicket updateTicket(ItTicket ticket);

    /**
     * 分配工单
     */
    ItTicket assignTicket(String ticketId, String handlerId, String handlerName);

    /**
     * 处理工单
     */
    ItTicket handleTicket(String ticketId, String solution, String status);

    /**
     * 关闭工单
     */
    ItTicket closeTicket(String ticketId, String remark);

    /**
     * 分页查询工单
     */
    Page<ItTicket> pageTickets(Integer pageNum, Integer pageSize, String status, String ticketType,
                                String priority, String applicantId, String handlerId, String keyword);
}
