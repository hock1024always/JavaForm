package com.homework.zx_msgservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendDTO {
    private String templateId;
    private String fillContent;
    private String carrierId;
    private String subject; // 主题
    private String content; // 内容
    private String receiver; // 可以是用户ID、用户列表、或其他表示接收人的结构
    private String cronExpression;
    private Integer runCount = 1; //发送次数 默认1
}