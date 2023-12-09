package io.github.dingxinliang88.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 演唱会参加信息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Data
@TableName(value = "concert_join")
@NoArgsConstructor
public class ConcertJoin {

    /**
     * 演唱会ID
     */
    @TableId(type = IdType.INPUT)
    private Integer concertId;

    /**
     * 歌迷ID
     */
    @TableId(type = IdType.INPUT)
    private Integer userId;
}
