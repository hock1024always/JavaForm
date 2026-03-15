package com.homework.topbiz.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * IT工单实体
 * @author JavaForm
 */
@Data
@TableName("it_ticket")
@Schema(description = "IT工单")
public class ItTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @Schema(description = "工单ID")
    private String id;

    @Schema(description = "工单标题")
    private String title;

    @Schema(description = "工单描述")
    private String description;

    @Schema(description = "工单类型: HARDWARE-硬件, SOFTWARE-软件, NETWORK-网络, ACCOUNT-账号, OTHER-其他")
    private String ticketType;

    @Schema(description = "优先级: LOW-低, MEDIUM-中, HIGH-高, URGENT-紧急")
    private String priority;

    @Schema(description = "状态: PENDING-待处理, PROCESSING-处理中, RESOLVED-已解决, CLOSED-已关闭, REJECTED-已拒绝")
    private String status;

    @Schema(description = "申请人ID")
    private String applicantId;

    @Schema(description = "申请人姓名")
    private String applicantName;

    @Schema(description = "处理人ID")
    private String handlerId;

    @Schema(description = "处理人姓名")
    private String handlerName;

    @Schema(description = "所属机构ID")
    private String institutionId;

    @Schema(description = "解决方案")
    private String solution;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "期望完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedTime;

    @Schema(description = "实际完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completedTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @Schema(description = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @Schema(description = "逻辑删除")
    @TableLogic
    private Integer deleted;
}
