package io.github.dingxinliang88.pojo.vo.song;

import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.pojo.po.Song;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 歌曲信息vo
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class SongInfoVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 歌曲ID
     */
    private Integer songId;

    /**
     * 歌曲名称
     */
    private String name;

    /**
     * 乐队ID
     */
    private Integer bandId;

    /**
     * 乐队名称
     */
    private String bandName;

    /**
     * 歌曲作者
     */
    private String author;

    /**
     * 专辑ID
     */
    private Integer albumId;

    /**
     * 所属专辑名称
     */
    private String albumName;

    /**
     * 是否可以喜欢
     */
    private Boolean canLike = Boolean.FALSE;

    /**
     * 是否喜欢
     */
    private Boolean isLiked;

    public static SongInfoVO songToVO(Song song) {
        SongInfoVO songInfoVO = new SongInfoVO();
        songInfoVO.setSongId(song.getSongId());
        songInfoVO.setName(song.getName());
        songInfoVO.setBandId(song.getBandId());
        songInfoVO.setAuthor(song.getAuthor());
        songInfoVO.setAlbumId(song.getAlbumId());
        songInfoVO.setAlbumName(song.getAlbumName());

        return songInfoVO;
    }
}
