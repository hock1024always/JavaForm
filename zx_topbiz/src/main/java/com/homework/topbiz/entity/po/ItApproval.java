package com.homework.topbiz.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审批流程实体
 * @author JavaForm
 */
@Data
@TableName("it_approval")
@Schema(description = "审批流程")
public class ItApproval implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @Schema(description = "审批ID")
    private String id;

    @Schema(description = "关联工单ID")
    private String ticketId;

    @Schema(description = "审批类型: TICKET_HANDLE-工单处理, ASSET_APPLY-资产申请, ASSET_RETURN-资产归还, OTHER-其他")
    private String approvalType;

    @Schema(description = "审批标题")
    private String title;

    @Schema(description = "审批内容")
    private String content;

    @Schema(description = "申请人ID")
    private String applicantId;

    @Schema(description = "申请人姓名")
    private String applicantName;

    @Schema(description = "审批人ID")
    private String approverId;

    @Schema(description = "审批人姓名")
    private String approverName;

    @Schema(description = "状态: PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝, CANCELLED-已取消")
    private String status;

    @Schema(description = "审批意见")
    private String opinion;

    @Schema(description = "审批时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvalTime;

    @Schema(description = "所属机构ID")
    private String institutionId;

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
