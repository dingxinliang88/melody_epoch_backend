package io.github.dingxinliang88.pojo.dto.album;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 歌曲录入专辑请求
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
public class SongToAlbumReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;


    /**
     * 专辑ID
     */
    @NotNull
    private Integer albumId;

    /**
     * 未选择的歌曲ID
     */
    private List<Integer> noneSelectedSongIds;

    /**
     * 选择的歌曲ID
     */
    private List<Integer> selectedSongIds;
}
