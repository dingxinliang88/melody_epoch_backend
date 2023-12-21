package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.po.AlbumLike;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Mapper
public interface AlbumLikeMapper extends BaseMapper<AlbumLike> {

    /**
     * 根据专辑ID和用户ID查询是否已经喜欢
     *
     * @param albumId 专辑ID
     * @param userId  用户ID
     * @return true - 已经喜欢
     */
    AlbumLike queryByAlbumIdAndUserId(Integer albumId, Integer userId);

    /**
     * 给专辑打分
     *
     * @param score   分数
     * @param albumId 专辑ID
     * @param userId  当前用户ID
     * @return true - 打分成功
     */
    Boolean updateScore(Float score, Integer albumId, Integer userId);

    /**
     * 根据专辑ID和用户ID查询删除
     *
     * @param albumId 专辑ID
     * @param userId  用户ID
     * @return true - 删除成功
     */
    Boolean deleteByAlbumIdAndUserId(Integer albumId, Integer userId);

    /**
     * 查询我喜欢的专辑
     *
     * @param userId 当前用户ID
     * @return like item list
     */
    List<AlbumInfoVO> listMyLikedAlbum(Integer userId);

    /**
     * 根据专辑搜索专辑喜欢信息
     *
     * @param albumId album id
     * @return album like list
     */
    List<AlbumLike> queryByAlbumId(Integer albumId);

}
