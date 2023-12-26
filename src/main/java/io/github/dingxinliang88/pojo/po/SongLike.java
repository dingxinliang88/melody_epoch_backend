package io.github.dingxinliang88.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 乐迷喜欢歌曲信息
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
@TableName(value = "song_like")
@NoArgsConstructor
public class SongLike {

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 歌迷ID
     */
    private Integer userId;

    /**
     * 专辑ID
     */
    private Integer songId;
}
