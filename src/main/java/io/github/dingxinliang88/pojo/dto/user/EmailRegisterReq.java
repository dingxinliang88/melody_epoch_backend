package io.github.dingxinliang88.pojo.dto.user;

import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static io.github.dingxinliang88.constants.UserConstant.PWD_MAX_LEN;
import static io.github.dingxinliang88.constants.UserConstant.PWD_MIN_LEN;

/**
 * 邮箱注册
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
public class EmailRegisterReq implements Serializable {


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
    @Length(min = 6, max = 6, message = "校验码长度为6位")
    private String code;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空！")
    @Length(min = PWD_MIN_LEN, max = PWD_MAX_LEN, message = "密码长度必须在8-16之间！")
    private String password;

    /**
     * 校验密码
     */
    @NotNull(message = "密码不能为空！")
    @Length(min = PWD_MIN_LEN, max = PWD_MAX_LEN, message = "校验密码长度必须在8-16之间， 且和密码相等！")
    private String checkedPassword;

    /**
     * 0 - guest, 1 - admin , 2 - member, 3 - fan
     *
     * @see io.github.dingxinliang88.pojo.enums.UserRoleType
     */
    private Integer type = UserRoleType.FAN.getType();

}
