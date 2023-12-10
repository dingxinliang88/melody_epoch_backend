package io.github.dingxinliang88.pojo.dto.user;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static io.github.dingxinliang88.constants.UserConstant.*;

/**
 * 账号登录请求体
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Data
public class AccLoginReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 账号
     */
    @NotNull(message = "账号不能为空！")
    @Length(min = ACC_MIN_LEN, max = ACC_MAX_LEN, message = "账号长度必须在5-16之间！")
    private String account;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空！")
    @Length(min = PWD_MIN_LEN, max = PWD_MAX_LEN, message = "密码长度必须在8-16之间！")
    private String password;
}
