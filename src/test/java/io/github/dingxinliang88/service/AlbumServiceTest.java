package io.github.dingxinliang88.service;

import cn.hutool.json.JSONUtil;
import io.github.dingxinliang88.mapper.CommentMapper;
import io.github.dingxinliang88.mapper.UserMapper;
import io.github.dingxinliang88.pojo.po.Comment;
import io.github.dingxinliang88.pojo.vo.comment.CommentVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AlbumServiceTest {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;


    @Test
    public void testComment() {
        Integer albumId = 1;
        List<Comment> comments = commentMapper.queryByAlbumId(albumId);
        List<CommentVO> commentVOList = parseComments(comments);
        System.out.println(JSONUtil.toJsonStr(commentVOList));
    }

    private List<CommentVO> parseComments(List<Comment> comments) {
        // Step 1: Create a map to store comments grouped by parent comment ID
        Map<Integer, List<Comment>> groupedComments = comments.stream()
                .collect(Collectors.groupingBy(Comment::getParentId));

        // Step 2: Process top-level comments and create CommentVO objects
        List<CommentVO> result = new ArrayList<>();
        List<Comment> topLevelComments = groupedComments.getOrDefault(0, Collections.emptyList());

        for (Comment topLevelComment : topLevelComments) {
            CommentVO commentVO = convertToCommentVO(topLevelComment);
            processChildComments(commentVO, groupedComments);
            result.add(commentVO);
        }

        // Step 3: Sort the top-level comments based on creation time
        result.sort(Comparator.comparing(CommentVO::getCreateTime, Comparator.reverseOrder()));
        return result;
    }

    private CommentVO convertToCommentVO(Comment comment) {
        // Fetch user name from user table using userId
        String userName = userMapper.queryNickNameByUserId(comment.getUserId());

        // Create CommentVO
        CommentVO commentVO = new CommentVO();
        commentVO.setCommentId(comment.getCommentId());
        commentVO.setAlbumId(comment.getAlbumId());
        commentVO.setContent(comment.getContent());
        commentVO.setParentId(comment.getParentId());
        commentVO.setUserId(comment.getUserId());
        commentVO.setUserName(userName); // Set the user name
        commentVO.setCreateTime(comment.getCreateTime());
        return commentVO;
    }

    private void processChildComments(CommentVO parentCommentVO, Map<Integer, List<Comment>> groupedComments) {
        List<Comment> childComments = groupedComments.getOrDefault(parentCommentVO.getCommentId(), Collections.emptyList());
        // Sort child comments based on creation time in descending order
        childComments.sort(Comparator.comparing(Comment::getCreateTime, Comparator.reverseOrder()));
        for (Comment childComment : childComments) {
            CommentVO childCommentVO = convertToCommentVO(childComment);
            processChildComments(childCommentVO, groupedComments);
            // Initialize the childrenComments list if it's null
            if (parentCommentVO.getChildrenComments() == null) {
                parentCommentVO.setChildrenComments(new ArrayList<>());
            }
            parentCommentVO.getChildrenComments().add(childCommentVO);
        }
    }


}