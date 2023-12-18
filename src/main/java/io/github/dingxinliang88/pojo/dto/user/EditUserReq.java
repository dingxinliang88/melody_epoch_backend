package io.github.dingxinliang88.pojo.dto.user;

import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.pojo.dto.fan.EditFanReq;
import io.github.dingxinliang88.pojo.dto.member.EditMemberReq;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class EditUserReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 用户ID
     */
    @NotNull
    private Integer userId;

    /**
     * 昵称
     */
    @NotNull(message = "昵称不能为空")
    private String nickname;

    /**
     * 用户类型
     */
    @NotNull
    private Integer type;

    /**
     * 乐队成员特有的属性
     */
    private EditMemberReq editMemberReq;

    /**
     * 乐迷特有的属性
     */
    private EditFanReq editFanReq;

}
