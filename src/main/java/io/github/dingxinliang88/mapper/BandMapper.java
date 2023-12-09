package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.po.Band;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Mapper
public interface BandMapper extends BaseMapper<Band> {

    /**
     * 根据乐队ID查找乐队（内部使用）
     *
     * @param bandId 乐队ID
     * @return band info
     */
    Band queryByBandIdInner(Integer bandId);

    /**
     * 根据队长ID查找乐队（内部使用）
     *
     * @param leaderId 队长ID
     * @return band info
     */
    Band queryByLeaderIdInner(Integer leaderId);


    /**
     * 修改乐队人数
     *
     * @param incr 新增人数
     * @return true - 修改成功
     */
    Boolean updateMemberNum(Integer bandId, int incr);

    /**
     * 变更队长ID
     *
     * @param bandId   乐队ID
     * @param leaderId 队长ID
     * @return true - 修改成功
     */
    Boolean updateLeaderId(Integer bandId, Integer leaderId);

    /**
     * 解散队伍
     *
     * @param bandId 乐队ID
     * @return true - 解散成功
     */
    Boolean disband(Integer bandId);
}
