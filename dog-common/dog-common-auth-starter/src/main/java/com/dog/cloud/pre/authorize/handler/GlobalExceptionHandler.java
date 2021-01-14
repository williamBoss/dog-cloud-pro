package com.dog.cloud.pre.authorize.handler;

import com.dog.cloud.core.exception.BaseException;
import com.dog.cloud.core.exception.CustomException;
import com.dog.cloud.core.exception.DemoModeException;
import com.dog.cloud.core.exception.PreAuthorizeException;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author KING
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 基础异常
     */
    @ExceptionHandler(BaseException.class)
    public BaseResult<?> baseException(BaseException e) {
        return BaseResult.failure().message(e.getDefaultMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public BaseResult<?> businessException(CustomException e) {
        if (StringUtils.isNull(e.getCode())) {
            return BaseResult.failure().message(e.getMessage());
        }
        return BaseResult.failure().code(e.getCode()).message(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResult<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return BaseResult.failure().message(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public BaseResult<?> validatedBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return BaseResult.failure().message(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return BaseResult.failure().message(message);
    }

    /**
     * 权限异常
     */
    @ExceptionHandler(PreAuthorizeException.class)
    public BaseResult<?> preAuthorizeException(PreAuthorizeException e) {
        return BaseResult.failure().message("没有权限，请联系管理员授权");
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public BaseResult<?> demoModeException(DemoModeException e) {
        return BaseResult.failure().message("演示模式，不允许操作");
    }
}
