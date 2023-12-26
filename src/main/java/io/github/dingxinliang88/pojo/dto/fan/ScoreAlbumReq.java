package io.github.dingxinliang88.pojo.dto.fan;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 给专辑打分请求
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
public class ScoreAlbumReq implements Serializable {
    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 专辑ID
     */
    @NotNull
    private Integer albumId;

    /**
     * 分数
     */
    @NotNull(message = "分数不能为空")
    @Max(value = 10)
    @Min(value = 1)
    private Float score;

}
