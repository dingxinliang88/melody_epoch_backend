package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.mapper.AlbumMapper;
import io.github.dingxinliang88.mapper.CommentMapper;
import io.github.dingxinliang88.pojo.dto.comment.AddCommentReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Album;
import io.github.dingxinliang88.pojo.po.Comment;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.service.CommentService;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Comment Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private AlbumMapper albumMapper;

    @Override
    public Integer addComment(AddCommentReq req, HttpServletRequest request) {
        // 获取当前登录用户
        UserLoginVO currUser = SysUtil.getCurrUser();

        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR, "禁止的操作");

        Integer albumId = req.getAlbumId();
        Album album = albumMapper.queryAlbumByAlbumId(albumId, false);
        ThrowUtil.throwIf(album == null, StatusCode.NOT_FOUND_ERROR, "该专辑不存在");

        Comment comment = new Comment(albumId, req.getParentId(), req.getContent(), currUser.getUserId(), LocalDateTime.now());
        // 顶级评论默认的父级ID
        comment.setParentId(req.getParentId() == null ? 0 : req.getParentId());
        commentMapper.insert(comment);

        return comment.getCommentId();
    }
}
