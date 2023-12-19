package io.github.dingxinliang88.pojo.vo.concert;

import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.pojo.po.Concert;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
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
     * 举办乐队ID
     */
    private Integer bandId; // band_id => bandId

    /**
     * 举办乐队名称
     */
    private String bandName;

    /**
     * 演唱会上限人数
     */
    private Integer maxNum;

    /**
     * 已加入人数
     */
    private Integer joinedNum;

    /**
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
    }
}
