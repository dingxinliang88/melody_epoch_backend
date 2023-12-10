package io.github.dingxinliang88.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 演唱会信息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Data
@TableName(value = "concert")
@NoArgsConstructor
public class Concert {

    /**
     * 演唱会ID
     */
    @TableId(type = IdType.AUTO)
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
     * 乐队ID
     */
    private Integer bandId;

    /**
     * 举办乐队名称
     */
    private String bandName;

    /**
     * 歌曲ID List，逗号分隔
     */
    private String songIdsStr;

    /**
     * 演唱会上限人数
     */
    private Integer maxNum;

    /**
     * 是否已经发布， 0 - 未发布， 1 - 发布
     */
    private Integer isRelease;

    /**
     * 逻辑删除标志 0 - 未删除， 1 - 删除
     */
    @TableLogic
    private Integer isDelete;

    public Concert(String name, LocalDateTime startTime, LocalDateTime endTime, String place, Integer bandId, String bandName, String songIdsStr, Integer maxNum) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.bandId = bandId;
        this.bandName = bandName;
        this.songIdsStr = songIdsStr;
        this.maxNum = maxNum;
    }
}
