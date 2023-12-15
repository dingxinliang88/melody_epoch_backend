package io.github.dingxinliang88.pojo.dto.comment;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 增加评论请求
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class AddCommentReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 专辑ID
     */
    @NotNull
    private Integer albumId;

    /**
     * 父级评论ID
     */
    private Integer parentId;

    /**
     * 评论信息
     */
    @NotNull(message = "评论内容不能为空")
    @Length(max = 250, message = "评论内容不能超过250个字符")
    private String content;
}
