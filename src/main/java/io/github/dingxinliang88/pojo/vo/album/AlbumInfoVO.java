package io.github.dingxinliang88.pojo.vo.album;

import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.pojo.po.Album;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

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
     * 发行时间
     */
    private LocalDateTime releaseTime;

    /**
     * 可以喜欢标志
     */
    private Boolean canLike = Boolean.FALSE;

    /**
     * 是否喜欢
     */
    private Boolean isLiked;

    public static AlbumInfoVO albumToVO(Album album) {
        AlbumInfoVO albumInfoVO = new AlbumInfoVO();
        albumInfoVO.setAlbumId(album.getAlbumId());
        albumInfoVO.setName(album.getName());
        albumInfoVO.setCompany(albumInfoVO.getName());
        albumInfoVO.setBandName(album.getBandName());
        albumInfoVO.setAvgScore(album.getAvgScore());
        albumInfoVO.setReleaseTime(album.getReleaseTime());
        albumInfoVO.setIsRelease(album.getIsRelease());
        albumInfoVO.setProfile(album.getProfile());
        return albumInfoVO;
    }
}
