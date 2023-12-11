package io.github.dingxinliang88.pojo.vo.band;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 乐队简略消息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
@Builder
public class BandBriefInfoVO implements Serializable {

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

}
