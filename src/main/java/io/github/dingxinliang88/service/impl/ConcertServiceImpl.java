package io.github.dingxinliang88.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.mapper.*;
import io.github.dingxinliang88.pojo.dto.concert.AddConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.EditConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.ReleaseConcertReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.Concert;
import io.github.dingxinliang88.pojo.po.ConcertJoin;
import io.github.dingxinliang88.pojo.po.SongLike;
import io.github.dingxinliang88.pojo.vo.concert.ConcertDetailsVO;
import io.github.dingxinliang88.pojo.vo.concert.ConcertInfoVO;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.service.ConcertService;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private SongMapper songMapper;

    @Resource
    private BandMapper bandMapper;

    @Resource
    private ConcertJoinMapper concertJoinMapper;

    @Resource
    private SongLikeMapper songLikeMapper;

    @Override
    public Long addConcert(AddConcertReq req, HttpServletRequest request) {
        // 判断当前登录用户是否是队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "您不是队长，无法添加演出");

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
        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "无权访问");

        // 判断演出时间是否合理（晚于当前时间，至少持续两个小时）
        ThrowUtil.throwIf(LocalDateTime.now().isAfter(req.getStartTime()), StatusCode.PARAMS_ERROR, "演出开始时间不能早于当前时间");
        ThrowUtil.throwIf(req.getStartTime().plusHours(2).isAfter(req.getEndTime()), StatusCode.PARAMS_ERROR, "演出时间不能少于2小时");

        String songIdsStr = StrUtil.join(",", req.getSongIdList());

        return concertMapper.editInfo(req.getConcertId(), req.getName(), req.getStartTime(),
                req.getEndTime(), band.getBandId(), req.getPlace(), songIdsStr, req.getMaxNum());

    }

    @Override
    public List<ConcertInfoVO> listConcertInfoVO(HttpServletRequest request) {
        return concertMapper.listConcertInfoVO();
    }

    @Override
    public Boolean releaseConcert(ReleaseConcertReq req, HttpServletRequest request) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改专辑信息!");

        Long concertId = req.getConcertId();
        Concert concert = concertMapper.queryByConcertId(concertId, true);
        ThrowUtil.throwIf(!concert.getBandId().equals(band.getBandId()), StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改演出信息!");
        // concert时间合理
        ThrowUtil.throwIf(concert.getStartTime().plusHours(-2).isBefore(LocalDateTime.now()), StatusCode.PARAMS_ERROR, "当前时间不合理");
        return concertMapper.updateReleaseStatusByConcertId(concertId, CommonConstant.RELEASE);
    }

    @Override
    public Boolean unReleaseConcert(ReleaseConcertReq req, HttpServletRequest request) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改专辑信息!");

        Long concertId = req.getConcertId();
        Concert concert = concertMapper.queryByConcertId(concertId, true);
        ThrowUtil.throwIf(!concert.getBandId().equals(band.getBandId()), StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改演出信息!");

        // concert 时间合理
        ThrowUtil.throwIf(concert.getStartTime().plusHours(-2).isBefore(LocalDateTime.now()), StatusCode.PARAMS_ERROR, "当前时间不合理");
        return concertMapper.updateReleaseStatusByConcertId(concertId, CommonConstant.UN_RELEASE);
    }

    @Override
    public List<ConcertInfoVO> getCurrConcertInfo(HttpServletRequest request) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改专辑信息!");

        List<ConcertInfoVO> concertInfoVOList = concertMapper.queryConcertByBandId(band.getBandId());

        return concertInfoVOList.stream().peek(concertInfoVO -> {
            LocalDateTime startTime = concertInfoVO.getStartTime();
            concertInfoVO.setCanEdit(
                    startTime.plusHours(2).isBefore(LocalDateTime.now())
            );
        }).collect(Collectors.toList());
    }

    @Override
    public ConcertDetailsVO getCurrConcertDetails(Long concertId, HttpServletRequest request) {
        Concert concert = concertMapper.queryByConcertId(concertId, true);
        ThrowUtil.throwIf(concert == null, StatusCode.NO_AUTH_ERROR, "演唱会不存在!");

        ConcertDetailsVO concertDetailsVO = new ConcertDetailsVO(concert);

        UserLoginVO currUser = SysUtil.getCurrUser();
        if (UserRoleType.FAN.getType().equals(currUser.getType())) {
            concertDetailsVO.setCanJoin(Boolean.TRUE);
            ConcertJoin concertJoin = concertJoinMapper.queryByConcertIdAndUserId(concertId, currUser.getUserId());
            concertDetailsVO.setIsJoined(concertJoin != null);
        }

        // 设置人数
        concertDetailsVO.setJoinedNum(concertJoinMapper.queryCountByConcertId(concertId));

        // 处理歌曲
        String songIdsStr = concert.getSongIdsStr();
        List<Integer> songIds
                = Arrays.stream(songIdsStr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        List<SongInfoVO> songInfoVOList = songMapper.queryBatchBySongId(songIds);
        songInfoVOList = songInfoVOList.stream().peek(songInfoVO -> {
            if (UserRoleType.FAN.getType().equals(currUser.getType())) {
                songInfoVO.setCanLike(Boolean.TRUE);
                SongLike songLike = songLikeMapper.queryBySongIdAndUserId(songInfoVO.getSongId(), currUser.getUserId());
                songInfoVO.setIsLiked(songLike != null);
            }
        }).collect(Collectors.toList());

        concertDetailsVO.setSongInfoVOList(songInfoVOList);

        return concertDetailsVO;
    }
}
