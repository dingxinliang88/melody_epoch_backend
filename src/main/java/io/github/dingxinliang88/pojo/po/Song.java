package io.github.dingxinliang88.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 歌曲信息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Data
@TableName(value = "song")
@NoArgsConstructor
public class Song {

    /**
     * 歌曲ID
     */
    @TableId(type = IdType.AUTO)
    private Integer songId;

    /**
     * 歌曲名称
     */
    private String name;

    /**
     * 歌曲作者
     */
    private String author;

    /**
     * 所属专辑ID
     */
    private Integer albumId;

    /**
     * 所属专辑名称
     */
    private String albumName;

    /**
     * 逻辑删除标志 0 - 未删除， 1 - 删除
     */
    @TableLogic
    private Integer isDelete;
}
