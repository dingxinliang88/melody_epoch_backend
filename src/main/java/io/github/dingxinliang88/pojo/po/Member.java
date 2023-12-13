package io.github.dingxinliang88.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 乐队成员信息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Data
@TableName(value = "member")
@NoArgsConstructor
public class Member {

    /**
     * 乐队成员ID
     */
    @TableId(type = IdType.INPUT)
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
     * 所在乐队ID
     */
    private Integer bandId;

    /**
     * 所在乐队名称
     */
    private String bandName;

    /**
     * 所在乐队是否已经发布，0 - 未发布， 1 - 发布，未发布时， 和乐队有关的信息查询结果为 NULL
     */
    private Integer isRelease;

    /**
     * 逻辑删除标志 0 - 未删除， 1 - 删除
     */
    @TableLogic
    private Integer isDelete;

    public Member(Integer memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

}
