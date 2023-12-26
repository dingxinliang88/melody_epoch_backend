package io.github.dingxinliang88.pojo.vo.song;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 歌曲录入专辑VO信息
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class SongToAlbumVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 尚未录入专辑的歌曲
     */
    private List<SongItemVO> noneAlbumSongs;

    /**
     * 当前专辑已经录入的歌曲
     */
    private List<SongItemVO> albumSongs;

    public SongToAlbumVO(List<SongItemVO> noneAlbumSongs, List<SongItemVO> albumSongs) {
        this.noneAlbumSongs = noneAlbumSongs;
        this.albumSongs = albumSongs;
    }
}
