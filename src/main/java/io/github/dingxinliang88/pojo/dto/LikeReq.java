package io.github.dingxinliang88.pojo.dto;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 喜欢收藏请求
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
public class LikeReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 喜欢的ID
     * 乐队
     * 专辑
     * 歌曲
     */
    @NotNull
    private Integer likeId;

    /**
     * 喜欢的类型
     * 0 - 乐队
     * 1 - 专辑
     * 2 - 歌曲
     */
    @NotNull
    @Max(2)
    @Min(0)
    private Integer type;
}
