package io.github.dingxinliang88.pojo.dto.band;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 创建乐队请求
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class AddBandReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 乐队名称
     */
    @NotNull(message = "乐队名称不能为空")
    @Length(max = 30, message = "乐队名称长度不能超过30")
    private String bandName;

    /**
     * 队长ID
     * 默认是当前登录用户（创建人） || 管理员指派
     */
    private Integer leaderId;

    /**
     * 乐队简介
     */
    @Length(max = 250, message = "乐队简介长度不能超过250")
    private String profile;
}
