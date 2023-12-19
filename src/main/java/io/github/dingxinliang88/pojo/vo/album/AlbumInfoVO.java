package io.github.dingxinliang88.pojo.vo.album;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class AlbumInfoVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 专辑ID
     */
    private Integer albumId;

    /**
     * 专辑名称
     */
    private String name;

    /**
     * 发行公司
     */
    private String company;

    /**
     * 所属乐队名称
     */
    private String bandName;

    /**
     * 专辑均分
     */
    private Float avgScore;

    /**
     * 是否已经发布
     */
    private Integer isRelease;

    /**
     * 简介
     */
    private String profile;

    /**
     * 可以喜欢标志
     */
    private Boolean canLike = Boolean.FALSE;

    /**
     * 是否喜欢
     */
    private Boolean isLiked;
}
