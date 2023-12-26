package io.github.dingxinliang88.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 歌迷信息
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
@TableName(value = "fan")
@NoArgsConstructor
public class Fan {

    /**
     * 歌迷ID
     */
    @TableId(type = IdType.INPUT)
    private Integer fanId;

    /**
     * 歌迷姓名
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
     * 职业
     */
    private String career;

    /**
     * 学历
     */
    private String education;

    /**
     * 逻辑删除标志 0 - 未删除， 1 - 删除
     */
    @TableLogic
    private Integer isDelete;

    public Fan(Integer fanId, String name) {
        this.fanId = fanId;
        this.name = name;
    }
}
