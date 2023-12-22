package io.github.dingxinliang88.pojo.vo.album;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * top albums info vo
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class TopAlbumVO implements Serializable {

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
     * 乐队名称
     */
    private String bandName;

    /**
     * 次序
     */
    private Float avgScore;
}
