package io.github.dingxinliang88.pojo.vo.fan;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class LikeAlbumStatusVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 是否喜欢
     */
    private Boolean isLiked = Boolean.FALSE;

    /**
     * 是否已经打分
     */
    private Boolean isScored = Boolean.FALSE;

}
