package io.github.dingxinliang88.pojo.vo.concert;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
public class ConcertJoinInfoVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 演唱会ID
     */
    private Long concertId;

    /**
     * 已加入人数
     */
    private Integer joinedNum;

    /**
     * 是否可以加入
     */
    private Boolean canJoin = Boolean.FALSE;

    /**
     * 是否已经加入
     */
    private Boolean canLeave = Boolean.FALSE;
}
