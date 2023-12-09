package io.github.dingxinliang88.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 演唱会歌曲信息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Data
@TableName(value = "concert_song")
@NoArgsConstructor
public class ConcertSong {

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 演唱会ID
     */
    private Integer concertId;

    /**
     * 歌迷ID
     */
    private Integer bandId;

    /**
     * 歌曲ID List，逗号分隔
     */
    private String songIdList;
}
