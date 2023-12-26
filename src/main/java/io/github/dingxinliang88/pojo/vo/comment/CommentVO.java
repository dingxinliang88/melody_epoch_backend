package io.github.dingxinliang88.pojo.vo.comment;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论信息VO类
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
public class CommentVO implements Serializable {

    /**
     * 评论ID
     */
    private Integer commentId;

    /**
     * 专辑ID
     */
    private Integer albumId;

    /**
     * 评论信息
     */
    private String content;

    /**
     * 父级评论ID
     */
    private Integer parentId;

    /**
     * 评论人ID
     */
    private Integer userId;

    /**
     * 评论人Name
     */
    private String userName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 其下的子评论
     */
    private List<CommentVO> childrenComments;
}
