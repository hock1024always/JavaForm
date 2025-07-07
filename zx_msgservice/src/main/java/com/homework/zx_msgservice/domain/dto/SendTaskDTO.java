package com.homework.zx_msgservice.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendTaskDTO {
    private String content;
    private String subject;
    private String receiver; // 可以是用户ID、用户列表、或其他表示接收人的结构
    private String carrierId;
    private String dbId; //修改发送状态用

}