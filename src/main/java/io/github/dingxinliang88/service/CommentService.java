package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.comment.AddCommentReq;
import io.github.dingxinliang88.pojo.po.Comment;

import javax.servlet.http.HttpServletRequest;

/**
 * Comment Service
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public interface CommentService extends IService<Comment> {

    /**
     * 用户评论专辑
     *
     * @param req     评论请求
     * @param request http request
     * @return comment id
     */
    Integer addComment(AddCommentReq req, HttpServletRequest request);
}
