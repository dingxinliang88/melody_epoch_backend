package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.po.SongLike;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Mapper
public interface SongLikeMapper extends BaseMapper<SongLike> {
    /**
     * 根据歌曲ID和用户ID查询是否已经喜欢
     *
     * @param songId 乐队ID
     * @param userId 用户ID
     * @return true - 已经喜欢
     */
    SongLike queryBySongIdAndUserId(Integer songId, Integer userId);

    /**
     * 根据歌曲ID和用户ID查询删除
     *
     * @param songId 歌曲ID
     * @param userId 用户ID
     * @return true - 删除成功
     */
    Boolean deleteBySongIdAndUserId(Integer songId, Integer userId);

    /**
     * 查询我喜欢的歌曲信息
     *
     * @param userId 当前用户ID
     * @return like item list
     */
    List<SongInfoVO> listMyLikedSong(Integer userId);
}
