package io.github.dingxinliang88.pojo.dto.user;

import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 邮箱登录请求体
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Data
public class EmailLoginReq implements Serializable {


    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 邮箱
     */
    @Email
    @NotNull(message = "邮箱不能为空")
    private String email;

    /**
     * 6位校验码
     */
    @NotNull(message = "校验码不能为空")
    @Length(min = 6, max = 6, message = "校验码长度为6位")
    private String code;

    /**
     * 0 - guest, 1 - admin , 2 - member, 3 - fan
     *
     * @see io.github.dingxinliang88.pojo.enums.UserRoleType
     */
    private Integer type = UserRoleType.FAN.getType();
}
