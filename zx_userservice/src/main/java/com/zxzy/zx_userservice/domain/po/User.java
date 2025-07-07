package com.zxzy.zx_userservice.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户实体类
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 用户的登录名称
     */
    private String username;

    /**
     * 用户登录密码，存储时加盐加密
     */
    private String password;

    /**
     * 用户的电子邮箱地址
     */
    private String email;

    /**
     * 用户的手机号码
     */
    private String phone;

    /**
     * 用户的个人简介
     */
    private String bio;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 最后更新记录的用户ID
     */
    private String updateBy;

    /**
     * 记录最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 扩展字段
     */

    // 自定义序列化方法
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // 序列化LocalDateTime为字符串
        out.writeObject(createTime != null ? createTime.toString() : null);
        out.writeObject(updateTime != null ? updateTime.toString() : null);
    }

    // 自定义反序列化方法
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // 反序列化字符串为LocalDateTime
        String createTimeStr = (String) in.readObject();
        String updateTimeStr = (String) in.readObject();
        this.createTime = createTimeStr != null ? LocalDateTime.parse(createTimeStr) : null;
        this.updateTime = updateTimeStr != null ? LocalDateTime.parse(updateTimeStr) : null;
    }
}