package io.github.dingxinliang88.pojo.vo.band;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 乐队详细消息
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class BandDetailsVO implements Serializable {

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
     * 乐队简介
     */
    private String profile;

    /**
     * 是否发布
     */
    private Integer isRelease;

    /**
     * 是否可以喜欢
     */
    private Boolean canLike = Boolean.FALSE;

    /**
     * 是否喜欢
     */
    private Boolean isLiked = Boolean.FALSE;

    /**
     * 是否可以加入
     */
    private Boolean canJoin = Boolean.FALSE;

    /**
     * 是否已经加入
     */
    private Boolean isJoined = Boolean.FALSE;
}
