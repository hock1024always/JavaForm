package com.homework.topbiz.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@AllArgsConstructor// 有参构造函数
@NoArgsConstructor//无参构造函数

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
}
