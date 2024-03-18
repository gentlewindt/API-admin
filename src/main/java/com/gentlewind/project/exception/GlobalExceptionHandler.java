package com.gentlewind.project.exception;

import com.gentlewind.project.common.BaseResponse;
import com.gentlewind.project.common.ErrorCode;
import com.gentlewind.project.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author gentlewind
 */
@RestControllerAdvice // 先捕获整个应用程序中抛出的异常，然后将异常处理方法的返回值自动转换成http响应的主体
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class) // 用于指定什么异常需要被捕获
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
