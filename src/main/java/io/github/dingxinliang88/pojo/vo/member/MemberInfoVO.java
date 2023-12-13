package io.github.dingxinliang88.pojo.vo.member;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 成员信息VO类
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberInfoVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 乐队成员ID
     */
    private Integer memberId;

    /**
     * 乐队成员姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 乐队成员分工
     */
    private String part;

    /**
     * 加入乐队时间
     */
    private LocalDateTime joinTime;

    /**
     * 离开乐队时间
     */
    private LocalDateTime leaveTime;

    /**
     * 所在乐队名称
     */
    private String bandName;

    public MemberInfoVO(Integer memberId, String name, Integer gender, Integer age, String part, LocalDateTime joinTime, LocalDateTime leaveTime, String bandName) {
        this.memberId = memberId;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.part = part;
        this.joinTime = joinTime;
        this.leaveTime = leaveTime;
        this.bandName = bandName;
    }
}
