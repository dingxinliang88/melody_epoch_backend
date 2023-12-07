package io.github.dingxinliang88.pojo.vo;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户登陆态保存信息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
@Builder
public class UserLoginVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 0 - guest, 1 - admin , 2 - member, 3 - fan
     *
     * @see io.github.dingxinliang88.pojo.enums.UserRoleType
     */
    private Integer type;

    /**
     * 账号
     */
    private String nickname;
}
