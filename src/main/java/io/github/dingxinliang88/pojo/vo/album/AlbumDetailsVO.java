package io.github.dingxinliang88.pojo.vo.album;

import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.pojo.po.Album;
import io.github.dingxinliang88.pojo.vo.comment.CommentVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 专辑详细信息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class AlbumDetailsVO implements Serializable {

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
     * 发表时间，跟随发布标志一起
     */
    private LocalDateTime releaseTime;

    /**
     * 所属乐队名称
     */
    private String bandName;

    /**
     * 专辑简介
     */
    private String profile;

    /**
     * 专辑均分
     */
    private Float avgScore;

    /**
     * 是否可以评论
     */
    private Boolean canComment;

    /**
     * 专辑评论信息
     */
    private List<CommentVO> commentVOList;

    public AlbumDetailsVO(Album album) {
        this.albumId = album.getAlbumId();
        this.name = album.getName();
        this.company = album.getCompany();
        this.releaseTime = album.getReleaseTime();
        this.bandName = album.getBandName();
        this.profile = album.getProfile();
        this.avgScore = album.getAvgScore();
    }
}
