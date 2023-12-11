package io.github.dingxinliang88.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.mapper.ConcertMapper;
import io.github.dingxinliang88.pojo.dto.concert.AddConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.EditConcertReq;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.Concert;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.service.ConcertService;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Comment Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class ConcertServiceImpl extends ServiceImpl<ConcertMapper, Concert>
        implements ConcertService {

    @Resource
    private ConcertMapper concertMapper;

    @Resource
    private BandMapper bandMapper;

    @Override
    public Long addConcert(AddConcertReq req, HttpServletRequest request) {
        // 判断当前登录用户是否是队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Band band = bandMapper.queryByBandIdInner(req.getBandId());
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "该乐队不存在");
        ThrowUtil.throwIf(!band.getLeaderId().equals(currUser.getUserId()), StatusCode.NO_AUTH_ERROR, "您不是队长，无法添加演出");

        // 判断演出时间是否合理（晚于当前时间，至少持续两个小时）
        ThrowUtil.throwIf(LocalDateTime.now().isAfter(req.getStartTime()), StatusCode.PARAMS_ERROR, "演出开始时间不能早于当前时间");
        ThrowUtil.throwIf(req.getStartTime().plusHours(2).isAfter(req.getEndTime()), StatusCode.PARAMS_ERROR, "演出时间不能少于2小时");

        String songIdsStr = StrUtil.join(",", req.getSongIdList());

        Concert concert = new Concert(req.getName(), req.getStartTime(), req.getEndTime(), req.getPlace(),
                band.getBandId(), band.getName(), songIdsStr, req.getMaxNum());

        concertMapper.insert(concert);

        return concert.getConcertId();
    }

    @Override
    public Boolean editInfo(EditConcertReq req, HttpServletRequest request) {
        // 判断当前登录用户是否是队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Band band = bandMapper.queryByBandIdInner(req.getBandId());
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "该乐队不存在");
        ThrowUtil.throwIf(!band.getLeaderId().equals(currUser.getUserId()), StatusCode.NO_AUTH_ERROR, "您不是队长，无法添加演出");

        // 判断演出时间是否合理（晚于当前时间，至少持续两个小时）
        ThrowUtil.throwIf(LocalDateTime.now().isAfter(req.getStartTime()), StatusCode.PARAMS_ERROR, "演出开始时间不能早于当前时间");
        ThrowUtil.throwIf(req.getStartTime().plusHours(2).isAfter(req.getEndTime()), StatusCode.PARAMS_ERROR, "演出时间不能少于2小时");

        String songIdsStr = StrUtil.join(",", req.getSongIdList());

        return concertMapper.editInfo(req.getConcertId(), req.getName(), req.getStartTime(),
                req.getEndTime(), req.getBandId(), req.getPlace(), songIdsStr, req.getMaxNum());

    }
}
