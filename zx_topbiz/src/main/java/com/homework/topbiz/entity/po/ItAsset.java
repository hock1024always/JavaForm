package com.homework.topbiz.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * IT资产实体
 * @author JavaForm
 */
@Data
@TableName("it_asset")
@Schema(description = "IT资产")
public class ItAsset implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @Schema(description = "资产ID")
    private String id;

    @Schema(description = "资产编号")
    private String assetCode;

    @Schema(description = "资产名称")
    private String assetName;

    @Schema(description = "资产类型: COMPUTER-电脑, MONITOR-显示器, PRINTER-打印机, NETWORK-网络设备, SERVER-服务器, OTHER-其他")
    private String assetType;

    @Schema(description = "品牌")
    private String brand;

    @Schema(description = "型号")
    private String model;

    @Schema(description = "序列号")
    private String serialNumber;

    @Schema(description = "状态: AVAILABLE-可用, IN_USE-使用中, REPAIRING-维修中, SCRAPPED-已报废, LOST-已丢失")
    private String status;

    @Schema(description = "使用人ID")
    private String userId;

    @Schema(description = "使用人姓名")
    private String userName;

    @Schema(description = "所属机构ID")
    private String institutionId;

    @Schema(description = "存放位置")
    private String location;

    @Schema(description = "购买日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime purchaseDate;

    @Schema(description = "购买金额")
    private BigDecimal purchaseAmount;

    @Schema(description = "保修截止日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime warrantyExpiry;

    @Schema(description = "配置信息(JSON)")
    private String configuration;

    @Schema(description = "备注")
    private String remark;

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
