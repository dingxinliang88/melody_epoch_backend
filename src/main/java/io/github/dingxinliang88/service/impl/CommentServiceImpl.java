package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.mapper.CommentMapper;
import io.github.dingxinliang88.pojo.po.Comment;
import io.github.dingxinliang88.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

}
