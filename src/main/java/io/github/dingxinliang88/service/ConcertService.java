package io.github.dingxinliang88.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.mapper.*;
import io.github.dingxinliang88.pojo.dto.concert.AddConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.EditConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.JoinConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.ReleaseConcertReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.Concert;
import io.github.dingxinliang88.pojo.po.ConcertJoin;
import io.github.dingxinliang88.pojo.po.SongLike;
import io.github.dingxinliang88.pojo.vo.concert.ConcertDetailsVO;
import io.github.dingxinliang88.pojo.vo.concert.ConcertInfoVO;
import io.github.dingxinliang88.pojo.vo.concert.ConcertJoinInfoVO;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.github.dingxinliang88.constants.CommonConstant.SONGS_STR_SEPARATOR;

/**
 * Comment Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Service
public class ConcertService extends ServiceImpl<ConcertMapper, Concert> {

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

    /**
     * 添加演唱会信息
     *
     * @param req 添加演唱会请求
     * @return 演唱会ID
     */
    public Long addConcert(AddConcertReq req) {
        // 判断当前登录用户是否是队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "您不是队长，无法添加演出");

        // 判断演出时间是否合理（晚于当前时间，至少持续两个小时）
        LocalDateTime startTime = req.getStartTime();
        LocalDateTime endTime = req.getEndTime();
        // 将起始时间和结束时间转换为东八区的时区
        ZoneId shanghaiZone = ZoneId.of("Asia/Shanghai");
        startTime = startTime.atZone(ZoneOffset.UTC).withZoneSameInstant(shanghaiZone).toLocalDateTime();
        endTime = endTime.atZone(ZoneOffset.UTC).withZoneSameInstant(shanghaiZone).toLocalDateTime();

        ThrowUtil.throwIf(LocalDateTime.now().isAfter(startTime), StatusCode.PARAMS_ERROR, "演出开始时间不能早于当前时间");
        ThrowUtil.throwIf(startTime.plusHours(2).isAfter(endTime), StatusCode.PARAMS_ERROR, "演出时间不能少于2小时");

        String songIdsStr = StrUtil.join(SONGS_STR_SEPARATOR, req.getSongIdList());

        Concert concert = new Concert(req.getName(), startTime, endTime, req.getPlace(),
                band.getBandId(), band.getName(), songIdsStr, req.getMaxNum());

        concertMapper.insert(concert);

        return concert.getConcertId();
    }

    /**
     * 修改演唱会信息
     *
     * @param req 修改演唱会信息
     * @return true - 修改成功
     */
    public Boolean editConcertInfo(EditConcertReq req) {
        // 判断当前登录用户是否是队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "无权访问");

        // 判断演出时间是否合理（晚于当前时间，至少持续两个小时）
        ThrowUtil.throwIf(LocalDateTime.now().isAfter(req.getStartTime()), StatusCode.PARAMS_ERROR, "演出开始时间不能早于当前时间");
        ThrowUtil.throwIf(req.getStartTime().plusHours(2).isAfter(req.getEndTime()), StatusCode.PARAMS_ERROR, "演出时间不能少于2小时");

        String songIdsStr = StrUtil.join(SONGS_STR_SEPARATOR, req.getSongIdList());

        return concertMapper.editInfo(req.getConcertId(), req.getName(), req.getStartTime(),
                req.getEndTime(), band.getBandId(), req.getPlace(), songIdsStr, req.getMaxNum());

    }

    /**
     * 获取演唱会信息
     *
     * @return concert info vo
     */
    public List<ConcertInfoVO> listConcertInfoVO() {
        return concertMapper.listConcertInfoVO();
    }

    /**
     * 分页获取演唱会信息
     * 每页数量限制为15
     *
     * @param current 当前页码
     * @return concert info vo
     */
    public Page<ConcertInfoVO> listConcertInfoVOByPage(Integer current) {
        LambdaQueryWrapper<Concert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Concert::getIsRelease, CommonConstant.RELEASE);

        Page<Concert> concertPage = concertMapper.selectPage(new Page<>(current, CommonConstant.DEFAULT_PAGE_SIZE), queryWrapper);

        return convertConcertInfoVOPage(concertPage, false);
    }

    /**
     * 发布演唱会信息
     *
     * @param req 发布演唱会请求
     * @return concert info vo
     */
    public Boolean releaseConcert(ReleaseConcertReq req) {
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

    /**
     * 撤销发布演唱会信息
     *
     * @param req 发布演唱会请求
     * @return concert info vo
     */
    public Boolean unReleaseConcert(ReleaseConcertReq req) {
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

    /**
     * 获取当前乐队的演唱会信息
     *
     * @return concert info vo list
     */
    public List<ConcertInfoVO> getCurrConcertInfo() {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改专辑信息!");

        List<ConcertInfoVO> concertInfoVOList = concertMapper.queryConcertByBandId(band.getBandId());

        return concertInfoVOList.stream().peek(concertInfoVO -> {
            LocalDateTime startTime = concertInfoVO.getStartTime();
            concertInfoVO.setCanEdit(
                    LocalDateTime.now().isBefore(startTime.minusHours(1))
            );
        }).collect(Collectors.toList());
    }

    /**
     * 分页获取当前乐队的演唱会信息
     *
     * @param current 页码
     * @return concert info vo list
     */
    public Page<ConcertInfoVO> getCurrConcertInfoByPage(Integer current, Integer size) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改专辑信息!");

        LambdaQueryWrapper<Concert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Concert::getBandId, band.getBandId());
        Page<Concert> concertPage = concertMapper.selectPage(new Page<>(current, size), queryWrapper);

        return convertConcertInfoVOPage(concertPage, true);
    }

    /**
     * 分页获取指定乐队的演唱会信息
     *
     * @param bandId  band id
     * @param current 页码
     * @param size    每页数据量
     * @return concert info vo page
     */
    public Page<ConcertInfoVO> getBandConcertInfoByPage(Integer bandId, Integer current, Integer size) {
        Band band = bandMapper.queryByBandId(bandId, false);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "查询无果");
        LambdaQueryWrapper<Concert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Concert::getBandId, bandId);
        Page<Concert> concertPage = concertMapper.selectPage(new Page<>(current, size), queryWrapper);

        return convertConcertInfoVOPage(concertPage, true);
    }

    /**
     * 获取当前演唱会信息
     *
     * @param concertId concert id
     * @return concert details
     */
    public ConcertDetailsVO getCurrConcertDetails(Long concertId) {
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
                = Arrays.stream(songIdsStr.split(SONGS_STR_SEPARATOR)).map(Integer::parseInt).collect(Collectors.toList());
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

    /**
     * 加入演唱会
     *
     * @param req 加入演唱会请求
     * @return true - 加入成功
     */
    public Boolean joinConcert(JoinConcertReq req) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR, "禁止的操作");

        Long concertId = req.getConcertId();
        // 查询演唱会信息是否合法
        Concert concert = concertMapper.queryByConcertId(concertId, false);
        ThrowUtil.throwIf(concert == null, StatusCode.NOT_FOUND_ERROR, "查询无果！");
        Integer joinedNum = concertJoinMapper.queryCountByConcertId(concertId);
        boolean isExpired = LocalDateTime.now().isAfter(concert.getStartTime());
        ThrowUtil.throwIf((joinedNum != null && concert.getMaxNum() <= joinedNum) || isExpired, StatusCode.NO_AUTH_ERROR, "禁止的操作");

        ConcertJoin concertJoin = new ConcertJoin();
        concertJoin.setConcertId(concertId);
        concertJoin.setUserId(currUser.getUserId());
        return concertJoinMapper.insert(concertJoin) == 1;
    }

    /**
     * 取消加入演唱会
     *
     * @param req 加入演唱会请求
     * @return true - 取消成功
     */
    public Boolean leaveConcert(JoinConcertReq req) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR, "禁止的操作");

        Long concertId = req.getConcertId();
        // 查询演唱会信息是否合法
        Concert concert = concertMapper.queryByConcertId(concertId, false);
        ThrowUtil.throwIf(concert == null, StatusCode.NOT_FOUND_ERROR, "查询无果！");
        boolean isExpired = LocalDateTime.now().isAfter(concert.getStartTime());
        ThrowUtil.throwIf(isExpired, StatusCode.NO_AUTH_ERROR, "禁止的操作");

        ConcertJoin concertJoin = concertJoinMapper.queryByConcertIdAndUserId(concertId, currUser.getUserId());
        ThrowUtil.throwIf(concertJoin == null, StatusCode.NO_AUTH_ERROR, "禁止的操作");

        return concertJoinMapper.deleteById(concertJoin.getId()) == 1;
    }


    /**
     * 获取当前演唱会的加入状态
     *
     * @param concertId concert id
     * @return concert join info vo
     */
    public ConcertJoinInfoVO getCurrConcertJoinInfo(Long concertId) {
        Concert concert = concertMapper.queryByConcertId(concertId, false);
        ThrowUtil.throwIf(concert == null, StatusCode.NOT_FOUND_ERROR, "查询无果！");

        ConcertJoinInfoVO concertJoinInfoVO = new ConcertJoinInfoVO();
        concertJoinInfoVO.setConcertId(concertId);

        // 设置人数
        concertJoinInfoVO.setJoinedNum(concertJoinMapper.queryCountByConcertId(concertId));

        UserLoginVO currUser = SysUtil.getCurrUser();
        if (UserRoleType.FAN.getType().equals(currUser.getType())) {
            LocalDateTime startTime = concert.getStartTime();
            boolean validTime = LocalDateTime.now().isBefore(startTime);
            ConcertJoin concertJoin = concertJoinMapper.queryByConcertIdAndUserId(concertId, currUser.getUserId());
            concertJoinInfoVO.setCanJoin(validTime && Objects.isNull(concertJoin));
            // 设置是否退出
            concertJoinInfoVO.setCanLeave(validTime && Objects.nonNull(concertJoin));
        }

        return concertJoinInfoVO;
    }

    // --------------------------
    // private util functions
    // --------------------------

    private Page<ConcertInfoVO> convertConcertInfoVOPage(Page<Concert> concertPage, boolean curr) {
        Page<ConcertInfoVO> concertInfoVOPage
                = new Page<>(concertPage.getCurrent(), concertPage.getSize(), concertPage.getTotal(), concertPage.searchCount());

        List<ConcertInfoVO> concertInfoVOList = concertPage.getRecords().stream().map(ConcertInfoVO::concertToVO).collect(Collectors.toList());
        if (curr) {
            concertInfoVOList = concertInfoVOList.stream().peek(concertInfoVO -> {
                LocalDateTime startTime = concertInfoVO.getStartTime();
                concertInfoVO.setCanEdit(
                        LocalDateTime.now().isBefore(startTime.minusHours(1))
                );
            }).collect(Collectors.toList());
        } else {
            // 设置是否可以参加
            concertInfoVOList = concertInfoVOList.stream().peek(concertInfoVO -> {
                Integer joinedNum = concertJoinMapper.queryCountByConcertId(concertInfoVO.getConcertId());
                LocalDateTime startTime = concertInfoVO.getStartTime();
                boolean validTime = LocalDateTime.now().isBefore(startTime);
                if (Objects.isNull(joinedNum)) {
                    joinedNum = 0;
                }
                concertInfoVO.setCanJoin(
                        validTime && joinedNum < concertInfoVO.getMaxNum()
                );
                // 设置是否退出
                ConcertJoin concertJoin = concertJoinMapper.queryByConcertIdAndUserId(concertInfoVO.getConcertId(), SysUtil.getCurrUser().getUserId());
                concertInfoVO.setCanLeave(validTime && Objects.nonNull(concertJoin));
            }).collect(Collectors.toList());
        }
        concertInfoVOPage.setRecords(concertInfoVOList);
        return concertInfoVOPage;
    }


}
