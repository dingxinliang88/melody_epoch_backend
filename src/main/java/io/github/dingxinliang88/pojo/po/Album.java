package io.github.dingxinliang88.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 专辑信息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Data
@TableName(value = "album")
@NoArgsConstructor
public class Album {
    /**
     * 专辑ID
     */
    @TableId(type = IdType.AUTO)
    private Integer albumId;

    /**
     * 专辑名称
     */
    private String name;

    /**
     * 发行公司
     */
    private String company;

    /**
     * 发表时间，跟随发布标志一起
     */
    private LocalDateTime releaseTime;

    /**
     * 所属乐队名称
     */
    private String bandName;

    /**
     * 歌曲ID List，逗号分隔
     */
    private String songIdsStr;

    /**
     * 专辑简介
     */
    private String profile;

    /**
     * 专辑均分
     */
    private Float avgScore;

    /**
     * 是否已经发布， 0 - 未发布， 1 - 发布
     */
    private Integer isRelease;

    /**
     * 逻辑删除标志 0 - 未删除， 1 - 删除
     */
    @TableLogic
    private Integer isDelete;

    public Album(String name, String company, String bandName, String profile) {
        this.name = name;
        this.company = company;
        this.bandName = bandName;
        this.profile = profile;
    }
}
