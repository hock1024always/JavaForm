package com.homework.zx_logservice.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 后端统一返回结果
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResultVO<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //信息
    private T data; //数据


    public static <T> ResultVO<T> success(T object) {
        ResultVO<T> ResultVO = new ResultVO<T>();
        ResultVO.data = object;
        ResultVO.code = 1;
        return ResultVO;
    }

    public static <T> ResultVO<T> error(String msg) {
        ResultVO ResultVO = new ResultVO();
        ResultVO.msg = msg;
        ResultVO.code = 0;
        return ResultVO;
    }

}