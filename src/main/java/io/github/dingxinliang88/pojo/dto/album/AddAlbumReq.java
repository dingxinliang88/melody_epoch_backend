package io.github.dingxinliang88.pojo.dto.album;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加专辑请求
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
public class AddAlbumReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 专辑名称
     */
    @NotNull(message = "专辑名称不能为空")
    @Length(min = 1, max = 30, message = "专辑名称长度必须在1-30之间")
    private String name;

    /**
     * 发行公司
     */
    @NotNull(message = "发行公司不能为空")
    @Length(min = 1, max = 30, message = "发行公司长度必须在1-30之间")
    private String company;

    /**
     * 所属乐队名称（可以不传，管理员创建的话需要传递）
     */
    private String bandName;

    /**
     * 专辑简介
     */
    @Length(max = 250, message = "专辑简介长度不能超过250")
    private String profile;
}
