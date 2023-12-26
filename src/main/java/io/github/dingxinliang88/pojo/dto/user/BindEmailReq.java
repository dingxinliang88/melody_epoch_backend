package io.github.dingxinliang88.pojo.dto.user;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 绑定邮箱请求
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
public class BindEmailReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    @NotNull(message = "邮箱不能为空")
    private String email;

    @NotNull(message = "验证码不能为空")
    private String code;
}
