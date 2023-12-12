package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.dto.band.EditBandReq;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Mapper
public interface BandMapper extends BaseMapper<Band> {

    /**
     * 根据乐队ID查找乐队
     *
     * @param bandId 乐队ID
     * @param inner  是否是inner使用
     * @return band info
     */
    Band queryByBandId(Integer bandId, boolean inner);

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
     * 根据乐队ID查找乐队详细信息
     *
     * @param bandId 乐队ID
     * @return band info
     */
    BandInfoVO queryBandInfoVOByBandId(Integer bandId);


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

    /**
     * 修改乐队信息
     *
     * @param req 修改乐队信息请求
     * @return true - 修改成功
     */
    Boolean editInfo(EditBandReq req);

    /**
     * 获取已发布所有乐队信息
     *
     * @return band info
     */
    List<Band> listBandInfo();
}
