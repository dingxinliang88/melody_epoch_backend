package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.po.ConcertJoin;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Mapper
public interface ConcertJoinMapper extends BaseMapper<ConcertJoin> {

    /**
     * 根据演唱会ID和用户ID查询参加演唱会情况
     *
     * @param concertId 演唱会ID
     * @param userId    用户ID
     * @return concert join
     */
    ConcertJoin queryByConcertIdAndUserId(Long concertId, Integer userId);

    /**
     * 查询演唱会加入人数
     *
     * @param concertId 演唱会ID
     * @return 人数
     */
    Integer queryNumByConcertId(Long concertId);

}
