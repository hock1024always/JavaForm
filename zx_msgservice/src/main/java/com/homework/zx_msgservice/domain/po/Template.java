package com.homework.zx_msgservice.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author uuy
 * @since 2025-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("template")
public class Template implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息类型, eg: 通知、提醒、请求、反馈
     */
    private String type;

    /**
     * 模板内容，通过${sample}$标注替换内容
     */
    private String content;

    /**
     * 回调数据地址
     */
    private String callbackUrl;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 所属单位的ID
     */
    private String institutionId;

    /**
     * 对记录的描述或说明
     */
    private String description;

    /**
     * 记录的状态
     */
    private String status;

    /**
     * 创建记录的用户ID
     */
    private String createBy;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新记录的用户ID
     */
    private String updateBy;

    /**
     * 记录最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 扩展字段
     */
    private String extension;

}