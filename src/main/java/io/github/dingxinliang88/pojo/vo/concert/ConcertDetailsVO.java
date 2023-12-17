package io.github.dingxinliang88.pojo.vo.concert;

import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 演唱会信息VO
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class ConcertDetailsVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 演唱会ID
     */
    private Long concertId;

    /**
     * 演唱会名称
     */
    private String name;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 演唱会地点
     */
    private String place;

    /**
     * 举办乐队名称
     */
    private String bandName;

    /**
     * 已加入人数
     */
    private Integer joinedNum;

    /**
     * 演唱会上限人数
     */
    private Integer maxNum;

    /**
     * 演唱会歌曲信息
     */
    private List<SongInfoVO> songInfoVOList;

    /**
     * 是否允许加入（目前主要判断时间 和 人数）
     */
    private Boolean isAllowedJoin;

    /**
     * 当前角色是否已经加入该演唱会
     */
    private Boolean isJoined;

    public ConcertDetailsVO(Long concertId, String name, LocalDateTime startTime, LocalDateTime endTime, String place, String bandName, Integer maxNum) {
        this.concertId = concertId;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.bandName = bandName;
        this.maxNum = maxNum;
    }
}
