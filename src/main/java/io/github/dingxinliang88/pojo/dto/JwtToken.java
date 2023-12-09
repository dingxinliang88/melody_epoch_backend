package io.github.dingxinliang88.pojo.dto;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Data
public class JwtToken implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    private String token;
    private String refreshToken;

    public JwtToken(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
