package io.github.dingxinliang88.pojo.dto.member;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改分工请求（仅队长）
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class EditPartReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 乐队成员ID
     */
    @NotNull
    private Integer memberId;

    /**
     * 乐队成员分工
     */
    @NotNull(message = "分工不能为空")
    @Length(min = 1, max = 30, message = "分工长度必须介于 1 和 30 之间")
    private String part;


    /**
     * 所在乐队ID
     */
    @NotNull
    private Integer bandId;
}
