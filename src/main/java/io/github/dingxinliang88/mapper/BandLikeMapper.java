package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.BandLike;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Mapper
public interface BandLikeMapper extends BaseMapper<BandLike> {

    /**
     * 根据乐队ID和用户ID查询是否已经喜欢
     *
     * @param bandId 乐队ID
     * @param userId 用户ID
     * @return true - 已经喜欢
     */
    BandLike queryByBandIdAndUserId(Integer bandId, Integer userId);

    /**
     * 根据乐队ID和用户ID查询删除
     *
     * @param bandId 乐队ID
     * @param userId 用户ID
     * @return true - 删除成功
     */
    Boolean deleteByBandIdAndUserId(Integer bandId, Integer userId);

    /**
     * 查询我喜欢的乐队
     *
     * @param userId 当前用户ID
     * @return like item list
     */
    List<Band> listMyLikedBand(Integer userId);

}
