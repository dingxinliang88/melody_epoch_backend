package io.github.dingxinliang88.pojo.vo.concert;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 演唱会信息VO
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class ConcertInfoVO implements Serializable {

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
     * 是否发布
     */
    private Integer isRelease;

    /**
     * 是否允许修改
     * 开始时间前一个小时不让修改了
     */
    private Boolean canEdit;
}
