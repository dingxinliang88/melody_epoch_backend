package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.dto.fan.EditFanReq;
import io.github.dingxinliang88.pojo.po.Fan;
import io.github.dingxinliang88.pojo.vo.fan.FanInfoVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Mapper
public interface FanMapper extends BaseMapper<Fan> {

    /**
     * 根据乐迷ID查询乐迷信息
     *
     * @param fanId 乐迷ID
     * @return 乐迷信息
     */
    Fan queryByFanId(Integer fanId);

    /**
     * 修改个人信息
     *
     * @param req 个人信息
     * @return true - 修改成功
     */
    Boolean updateInfoByFanId(EditFanReq req);

    /**
     * 根据乐迷ID查询乐迷信息
     *
     * @param fanId 乐迷id
     * @return fan info vo
     */
    FanInfoVO queryFanInfoByFanId(Integer fanId);
}
