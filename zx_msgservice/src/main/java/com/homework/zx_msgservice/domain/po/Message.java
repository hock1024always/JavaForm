package com.homework.zx_msgservice.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Builder;
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
@TableName("message")
@Builder
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 载体id
     */
    private String carrierId;

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 模板填充内容，回调的数据等
     */
    private String templateFill;

    /**
     * 策略名称
     */
    private String strategyName;

    /**
     * 接受方，可能是群发
     */
    private String receiver;

    /**
     * 发送时间
     */
    private Long sendTime;

    private String content; //  内容

    private String subject; // 主题

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
