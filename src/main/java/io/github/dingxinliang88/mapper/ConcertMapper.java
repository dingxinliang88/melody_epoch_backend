package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.po.Concert;
import io.github.dingxinliang88.pojo.vo.concert.ConcertInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Mapper
public interface ConcertMapper extends BaseMapper<Concert> {
    /**
     * 修改演唱会信息
     *
     * @return true - 修改成功
     */
    Boolean editInfo(Long concertId, String name, LocalDateTime startTime,
                     LocalDateTime endTime, Integer bandId, String place, String songIdsStr, Integer maxNum);

    /**
     * 获取演唱会信息VO
     *
     * @return concert info vo list
     */
    List<ConcertInfoVO> listConcertInfoVO();

    /**
     * 根据乐队ID获取演唱会信息
     *
     * @param bandId 乐队ID
     * @return concert list
     */
    List<ConcertInfoVO> queryConcertByBandId(Integer bandId);

    /**
     * 修改演唱会发布状态
     *
     * @param bandId  band id
     * @param release release status
     * @return true - 修改成功
     */
    Boolean updateReleaseStatusByBandId(Integer bandId, Integer release);

    /**
     * 根据 concert id 查询 concert 信息
     *
     * @param concertId concert id
     * @return concert info
     */
    Concert queryByConcertId(Long concertId, boolean inner);

    /**
     * 修改演唱会发布状态
     *
     * @param concertId concert id
     * @param release   release status
     * @return true - 修改成功
     */
    Boolean updateReleaseStatusByConcertId(Long concertId, Integer release);
}
