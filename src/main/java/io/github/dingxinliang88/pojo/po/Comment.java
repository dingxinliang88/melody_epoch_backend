package io.github.dingxinliang88.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评论信息
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
@TableName(value = "comment")
@NoArgsConstructor
public class Comment {

    /**
     * 评论ID
     */
    @TableId(type = IdType.AUTO)
    private Integer commentId;

    /**
     * 专辑ID
     */
    private Integer albumId;

    /**
     * 父级评论ID
     */
    private Integer parentId;

    /**
     * 评论信息
     */
    private String content;

    /**
     * 评论人ID
     */
    private Integer userId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标志 0 - 未删除， 1 - 删除
     */
    @TableLogic
    private Integer isDelete;

    public Comment(Integer albumId, Integer parentId, String content, Integer userId,LocalDateTime createTime) {
        this.albumId = albumId;
        this.parentId = parentId;
        this.content = content;
        this.userId = userId;
        this.createTime = createTime;
    }
}
