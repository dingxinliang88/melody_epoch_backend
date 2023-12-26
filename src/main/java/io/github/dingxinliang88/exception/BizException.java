package io.github.dingxinliang88.exception;

import io.github.dingxinliang88.biz.StatusCode;
import lombok.Getter;

/**
 * 自定义业务常量类
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
public class BizException extends RuntimeException {

    /**
     * 状态码
     *
     * @see StatusCode
     */
    private final int code;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(StatusCode statusCode) {
        this(statusCode.getCode(), statusCode.getMessage());
    }

    public BizException(StatusCode statusCode, String message) {
        this(statusCode.getCode(), message);
    }
}
