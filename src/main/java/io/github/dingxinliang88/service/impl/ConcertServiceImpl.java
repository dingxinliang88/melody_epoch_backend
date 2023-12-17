package io.github.dingxinliang88.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.mapper.*;
import io.github.dingxinliang88.pojo.dto.concert.AddConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.EditConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.JoinConcertReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.Concert;
import io.github.dingxinliang88.pojo.po.ConcertJoin;
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
    private BandMapper bandMapper;

    @Resource
    private SongMapper songMapper;

    @Resource
    private SongLikeMapper songLikeMapper;

    @Resource
    private ConcertJoinMapper concertJoinMapper;

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
        Band band = bandMapper.queryByBandId(req.getBandId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "该乐队不存在");
        ThrowUtil.throwIf(!band.getLeaderId().equals(currUser.getUserId()), StatusCode.NO_AUTH_ERROR, "您不是队长，无法添加演出");

        // 判断演出时间是否合理（晚于当前时间，至少持续两个小时）
        ThrowUtil.throwIf(LocalDateTime.now().isAfter(req.getStartTime()), StatusCode.PARAMS_ERROR, "演出开始时间不能早于当前时间");
        ThrowUtil.throwIf(req.getStartTime().plusHours(2).isAfter(req.getEndTime()), StatusCode.PARAMS_ERROR, "演出时间不能少于2小时");

        String songIdsStr = StrUtil.join(",", req.getSongIdList());

        return concertMapper.editInfo(req.getConcertId(), req.getName(), req.getStartTime(),
                req.getEndTime(), req.getBandId(), req.getPlace(), songIdsStr, req.getMaxNum());

    }

    @Override
    public List<ConcertInfoVO> listConcertInfoVO(HttpServletRequest request) {
        return concertMapper.listConcertInfoVO();
    }

    @Override
    public ConcertDetailsVO listCurrConcertInfoVO(Long concertId, HttpServletRequest request) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        Concert concert = concertMapper.queryByConcertId(concertId);
        ConcertDetailsVO concertDetailsVO = new ConcertDetailsVO(
                concertId, concert.getName(), concert.getStartTime(),
                concert.getEndTime(), concert.getPlace(), concert.getBandName(), concert.getMaxNum()
        );
        String songIdsStr = concert.getSongIdsStr();
        if (StrUtil.isNotEmpty(songIdsStr)) {
            List<Integer> songIds = StrUtil.split(songIdsStr, ",").stream()
                    .map(Integer::parseInt).collect(Collectors.toList());
            List<SongInfoVO> songInfoVOList = songMapper.queryBatchSongInfoVO(songIds);
            concertDetailsVO.setSongInfoVOList(
                    songInfoVOList.stream()
                            .peek(songInfoVO ->
                                    songInfoVO.setIsLiked(songLikeMapper.queryBySongIdAndUserId(songInfoVO.getSongId(), currUser.getUserId()) != null))
                            .collect(Collectors.toList())
            );
        }

        // 判断当前登录用户是否已经加入该演唱会
        ConcertJoin concertJoin = concertJoinMapper.queryByConcertIdAndUserId(concertId, currUser.getUserId());
        concertDetailsVO.setIsJoined(concertJoin != null);
        Integer joinedNum = concertJoinMapper.queryNumByConcertId(concertId);
        concertDetailsVO.setJoinedNum(joinedNum);
        boolean isExpired = LocalDateTime.now().isAfter(concert.getStartTime());
        concertDetailsVO.setIsAllowedJoin(joinedNum <= concert.getMaxNum() && !isExpired);
        return concertDetailsVO;
    }

    @Override
    public Boolean joinConcert(JoinConcertReq req, HttpServletRequest request) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR, "禁止的操作");

        Long concertId = req.getConcertId();
        // 查询演唱会信息是否合法
        Concert concert = concertMapper.queryByConcertId(concertId);
        Integer joinedNum = concertJoinMapper.queryNumByConcertId(concertId);
        boolean isExpired = LocalDateTime.now().isAfter(concert.getStartTime());
        ThrowUtil.throwIf(concert.getMaxNum() <= joinedNum || isExpired, StatusCode.NO_AUTH_ERROR, "禁止的操作");

        ConcertJoin concertJoin = new ConcertJoin();
        concertJoin.setConcertId(concertId);
        concertJoin.setUserId(currUser.getUserId());
        return concertJoinMapper.insert(concertJoin) == 1;
    }

    @Override
    public Boolean leaveConcert(JoinConcertReq req, HttpServletRequest request) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR, "禁止的操作");

        Long concertId = req.getConcertId();
        // 查询演唱会信息是否合法
        Concert concert = concertMapper.queryByConcertId(concertId);
        boolean isExpired = LocalDateTime.now().isAfter(concert.getStartTime());
        ThrowUtil.throwIf(isExpired, StatusCode.NO_AUTH_ERROR, "禁止的操作");

        ConcertJoin concertJoin = concertJoinMapper.queryByConcertIdAndUserId(concertId, currUser.getUserId());
        ThrowUtil.throwIf(concertJoin == null, StatusCode.NO_AUTH_ERROR, "禁止的操作");

        return concertJoinMapper.deleteById(concertJoin.getId()) == 1;
    }
}
