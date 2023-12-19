package io.github.dingxinliang88.pojo.dto.band;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改乐队信息请求（仅队长）
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class EditBandReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 乐队ID
     */
    @NotNull
    private Integer bandId;

    /**
     * 乐队简介
     */
    @NotNull(message = "乐队简介不能为空")
    @Length(min = 1, max = 230, message = "乐队简介长度不能超过230个字符")
    private String profile;
}
