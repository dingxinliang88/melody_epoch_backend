package io.github.dingxinliang88.biz;

import lombok.Getter;
import lombok.Setter;

/**
 * 通用返回类
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class BaseResponse<T> {
    /**
     * 状态码
     *
     * @see StatusCode
     */
    private int code;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回信息
     */
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
