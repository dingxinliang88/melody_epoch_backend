package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.po.Concert;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

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
}
