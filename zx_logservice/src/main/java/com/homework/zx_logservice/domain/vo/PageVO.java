package com.homework.zx_logservice.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageVO<T>  {
    private Long pageNum;
    private Long pageSize;
    private Long total;
    private List<T> dataList;
}
