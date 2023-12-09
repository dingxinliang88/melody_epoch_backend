package io.github.dingxinliang88.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 乐迷喜欢专辑信息（包含打分信息）
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Data
@TableName(value = "album_like")
@NoArgsConstructor
public class AlbumLike {

    /**
     * 歌迷ID
     */
    @TableId(type = IdType.INPUT)
    private Integer userId;

    /**
     * 专辑ID
     */
    @TableId(type = IdType.INPUT)
    private Integer albumId;

    /**
     * 打分
     */
    private Float score;
}
