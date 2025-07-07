package com.zxzy.zx_userservice.exception;



import com.zxzy.zx_userservice.domain.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //业务逻辑异常处理
    @ExceptionHandler(LogicException.class)
    public ResultVO handleLogicException(LogicException e) {
        log.error("业务逻辑异常 " + e.getMessage());
        return ResultVO.error(e.getMessage());
    }
}
