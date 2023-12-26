package io.github.dingxinliang88.pojo.vo.song;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 歌曲元素VO （用于选歌）
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
public class SongItemVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 歌曲ID
     */
    private Integer songId;

    /**
     * 歌曲名称
     */
    private String name;
}
