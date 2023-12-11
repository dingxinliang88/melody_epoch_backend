package io.github.dingxinliang88.pojo.vo.band;

import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.pojo.po.Album;
import io.github.dingxinliang88.pojo.po.Concert;
import io.github.dingxinliang88.pojo.po.Member;
import io.github.dingxinliang88.pojo.po.Song;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 乐队详细消息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class BandInfoVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 乐队ID
     */
    private Integer bandId;

    /**
     * 乐队名称
     */
    private String name;

    /**
     * 乐队成立时间
     */
    private LocalDateTime foundTime;

    /**
     * 队长ID
     */
    private String leaderName;

    /**
     * 乐队人数
     */
    private Integer memberNum;

    /**
     * 成员信息
     */
    private List<Member> members;

    /**
     * 歌曲信息
     */
    private List<Song> songs;

    /**
     * 专辑信息
     */
    private List<Album> albums;

    /**
     * 演唱会信息
     */
    private List<Concert> concerts;
}
