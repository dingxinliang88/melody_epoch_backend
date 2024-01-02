package io.github.dingxinliang88.exception;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.utils.RespUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    public BaseResponse<?> handleBizException(BizException e) {
        logger.error("catch biz exception: ", e);
        return RespUtil.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public BaseResponse<?> bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> errMsg = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return RespUtil.error(StatusCode.BAD_REQUEST.getCode(), String.join(",", errMsg));
    }

    /**
     * 处理 json 请求体调用接口校验失败抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> errMsg = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        logger.error("catch json request body param validate exception: {}", errMsg);
        return RespUtil.error(StatusCode.BAD_REQUEST);
    }

    /**
     * 处理单个参数校验失败抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse<?> constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> errMsg = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        logger.error("catch single param validate exception: {}", errMsg);
        return RespUtil.error(StatusCode.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<?> handleOtherException(Exception e) {
        logger.error("catch  other exception: ", e);
        return RespUtil.error(StatusCode.SYSTEM_ERROR);
    }
}
