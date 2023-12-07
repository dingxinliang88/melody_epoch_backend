package io.github.dingxinliang88.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 乐队信息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Data
@TableName(value = "band")
@NoArgsConstructor
public class Band {

    /**
     * 乐队ID
     */
    @TableId(type = IdType.AUTO)
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
    private Integer leaderId;

    /**
     * 乐队简介
     */
    private String profile;

    /**
     * 乐队人数
     */
    private Integer memberNum;

    /**
     * 逻辑删除标志 0 - 未删除， 1 - 删除
     */
    @TableLogic
    private Integer isDelete;
}
