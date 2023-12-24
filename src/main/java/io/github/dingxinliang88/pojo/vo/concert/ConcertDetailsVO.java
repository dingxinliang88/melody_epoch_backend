package io.github.dingxinliang88.pojo.vo.concert;

import io.github.dingxinliang88.constants.CommonConstant;
<<<<<<< HEAD
import io.github.dingxinliang88.pojo.po.Concert;
=======
>>>>>>> fea_join
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
<<<<<<< HEAD
=======
 * 演唱会信息VO
 *
>>>>>>> fea_join
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
<<<<<<< HEAD
     * 举办乐队ID
     */
    private Integer bandId;

    /**
=======
>>>>>>> fea_join
     * 举办乐队名称
     */
    private String bandName;

    /**
<<<<<<< HEAD
     * 演唱会上限人数
     */
    private Integer maxNum;

    /**
=======
>>>>>>> fea_join
     * 已加入人数
     */
    private Integer joinedNum;

    /**
<<<<<<< HEAD
     * 歌曲信息
     */
    private List<SongInfoVO> songInfoVOList;

    private Boolean canJoin = Boolean.FALSE;
    private Boolean isJoined = Boolean.FALSE;


    public ConcertDetailsVO(Concert concert) {
        this.concertId = concert.getConcertId();
        this.name = concert.getName();
        this.startTime = concert.getStartTime();
        this.endTime = concert.getEndTime();
        this.place = concert.getPlace();
        this.bandId = concert.getBandId();
        this.bandName = concert.getBandName();
        this.maxNum = concert.getMaxNum();
=======
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
>>>>>>> fea_join
    }
}
