package io.github.dingxinliang88.exception;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.utils.RespUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    public BaseResponse<?> handleBizException(BizException e) {
        logger.error("catch biz exception: ", e);
        return RespUtil.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<?> handleOtherException(Exception e) {
        logger.error("catch  other exception: ", e);
        return RespUtil.error(StatusCode.SYSTEM_ERROR);
    }
}
