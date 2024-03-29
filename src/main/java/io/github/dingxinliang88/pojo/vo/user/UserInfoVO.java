package io.github.dingxinliang88.pojo.vo.user;

import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.pojo.po.User;
import io.github.dingxinliang88.pojo.vo.fan.FanInfoVO;
import io.github.dingxinliang88.pojo.vo.member.MemberInfoVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户类型
     */
    private Integer type;

    /**
     * 是否被封禁
     */
    private Boolean isBanned = Boolean.FALSE;

    /**
     * 乐队成员特有的属性
     */
    private MemberInfoVO memberInfoVO;

    /**
     * 乐迷特有的属性
     */
    private FanInfoVO fanInfoVO;

    public static UserInfoVO userToVO(User user) {
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUserId(user.getUserId());
        userInfoVO.setNickname(user.getNickname());
        userInfoVO.setEmail(user.getEmail());
        userInfoVO.setType(user.getType());
        return userInfoVO;
    }
}
