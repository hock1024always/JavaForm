package com.homework.topbiz.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CycleSendDTO {
    private String templateId;
    private String fillContent;
    private String carrierId;
    private String receiver; // 可以是用户ID、用户列表、或其他表示接收人的结构
    private String cronExpression;
    private long cycleTimes;
}