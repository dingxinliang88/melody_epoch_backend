package io.github.dingxinliang88.pojo.dto.email;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 获取邮件（验证码）请求
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
public class EmailCaptchaReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 乐队名称
     */
    @Email
    @NotNull(message = "邮箱不能为空")
    private String email;

}
