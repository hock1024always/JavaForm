package com.zxzy.zx_userservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBindGroupDTO {
    private String userId;
    private String groupId;
    private String relationName;
}