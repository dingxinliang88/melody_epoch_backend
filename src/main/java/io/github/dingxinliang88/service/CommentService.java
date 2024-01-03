package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.manager.SensitiveHandler;
import io.github.dingxinliang88.mapper.AlbumMapper;
import io.github.dingxinliang88.mapper.CommentMapper;
import io.github.dingxinliang88.pojo.dto.comment.AddCommentReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Album;
import io.github.dingxinliang88.pojo.po.Comment;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.utils.ContentUtil;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Comment Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private SensitiveHandler sensitiveHandler;

    /**
     * 用户评论专辑
     *
     * @param req 评论请求
     * @return comment id
     */
    public Integer addComment(AddCommentReq req) {
        // 获取当前登录用户
        UserLoginVO currUser = SysUtil.getCurrUser();

        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR, "禁止的操作");

        Integer albumId = req.getAlbumId();
        Album album = albumMapper.queryAlbumByAlbumId(albumId, false);
        ThrowUtil.throwIf(album == null, StatusCode.NOT_FOUND_ERROR, "该专辑不存在");

        // 过滤评论
        String content = req.getContent();
        String cleanContent = ContentUtil.cleanContent(content);

        // 敏感词计数器处理
        sensitiveHandler.handleAccSensitive(currUser.getUserId(), !content.equals(cleanContent));

        Comment comment = new Comment(albumId, req.getParentId(), cleanContent, currUser.getUserId(), LocalDateTime.now());
        // 顶级评论默认的父级ID
        comment.setParentId(Objects.nonNull(req.getParentId()) ? req.getParentId() : 0);
        commentMapper.insert(comment);

        return comment.getCommentId();
    }
}
