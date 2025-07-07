package com.zxzy.zx_userservice.domain.dto;

import com.zxzy.zx_userservice.domain.po.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GroupByUserDTO {

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
    private String extension;

    private Long pageNum;
    private Long pageSize;
    private Long total;
    private List<List<Object>> groupAndRelationNameMap;

}