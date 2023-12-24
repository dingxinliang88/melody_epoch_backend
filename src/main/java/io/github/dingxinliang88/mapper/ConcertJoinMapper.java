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
     * 根据演唱会ID和用户ID查询演唱会信息
     *
     * @param concertId concert id
     * @param userId    user id
     * @return concert join info
     */
    ConcertJoin queryByConcertIdAndUserId(Long concertId, Integer userId);

    /**
     * 查询演唱会参加人数
     *
     * @param concertId concert id
     * @return concert join count
     */
    Integer queryCountByConcertId(Long concertId);


}
