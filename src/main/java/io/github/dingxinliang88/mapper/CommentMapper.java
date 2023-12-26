package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.po.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 根据专辑ID查询评论信息
     *
     * @param albumId 专辑ID
     * @return 评论信息
     */
    List<Comment> queryByAlbumId(Integer albumId);
}
