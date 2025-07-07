package com.zxzy.zx_userservice.domain.dto;

import com.zxzy.zx_userservice.domain.po.GroupAndUser;
import com.zxzy.zx_userservice.domain.po.User;
import com.zxzy.zx_userservice.domain.vo.PageVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UsersInGroupDTO {
    private String id;

    /**
     * 分组名称
     */
    private String groupName;

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

    private String relationName;

    private PageVO<User> userPageVO;
}
