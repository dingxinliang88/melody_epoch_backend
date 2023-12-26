package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.exception.BizException;
import io.github.dingxinliang88.manager.SensitiveHandler;
import io.github.dingxinliang88.mapper.*;
import io.github.dingxinliang88.pojo.dto.band.AddBandReq;
import io.github.dingxinliang88.pojo.dto.band.EditBandReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.BandLike;
import io.github.dingxinliang88.pojo.po.Member;
import io.github.dingxinliang88.pojo.vo.band.BandDetailsVO;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.utils.ContentUtil;
import io.github.dingxinliang88.utils.RedisUtil;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.dingxinliang88.constants.UserConstant.USER_AUTH_TYPE_PREFIX;

/**
 * Band Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Service
public class BandService extends ServiceImpl<BandMapper, Band> {

    @Resource
    private BandMapper bandMapper;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private SongMapper songMapper;

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private ConcertMapper concertMapper;

    @Resource
    private BandLikeMapper bandLikeMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private SensitiveHandler sensitiveHandler;

    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * 创建乐队
     *
     * @param req 创建乐队请求
     * @return band id
     */
    public Integer addBand(AddBandReq req) {
        String bandName = req.getBandName();
        Integer leaderId = req.getLeaderId();
        String profile = req.getProfile();

        // 获取当前登录用户
        UserLoginVO currUser = SysUtil.getCurrUser();

        if (UserRoleType.MEMBER.getType().equals(currUser.getType())) {
            // 成员创建乐队
            leaderId = currUser.getUserId();
        } else if (UserRoleType.ADMIN.getType().equals(currUser.getType())) {
            // 是管理员，查找leaderId是否存在
            Member member = memberMapper.queryByMemberId(leaderId);
            ThrowUtil.throwIf(member == null, StatusCode.NOT_FOUND_ERROR, "找不到队长！");
        } else {
            throw new BizException(StatusCode.NO_AUTH_ERROR, "没有权限创建乐队！");
        }

        // 判断当前bandName是否已经被注册了
        Band bandFromDB = bandMapper.queryByBandName(bandName, true);
        ThrowUtil.throwIf(bandFromDB != null, StatusCode.PARAMS_ERROR, "当前乐队名称已经被注册");

        // 判断当前leaderId是否已经加入了队伍
        bandFromDB = bandMapper.queryByLeaderId(leaderId, true);
        ThrowUtil.throwIf(bandFromDB != null, StatusCode.DUPLICATE_DATA, "该队长已经加入乐队了！");

        final Integer leader = leaderId;
        return transactionTemplate.execute(status -> {
            try {
                Band band = new Band(bandName, leader, profile, LocalDateTime.now());
                band.setMemberNum(1);
                bandMapper.insert(band);
                // 修改对应的member信息，band_id、band_name
                memberMapper.updateBandIdAndBandName(leader, band.getBandId(), band.getName(),
                        LocalDateTime.now(), null, CommonConstant.UN_RELEASE);
                // 删除leader的权限缓存
                redisUtil.delete(USER_AUTH_TYPE_PREFIX + leader);
                return band.getBandId();
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    /**
     * 修改乐队信息（仅队长）
     *
     * @param req 修改乐队信息请求
     * @return true - 修改成功
     */
    public Boolean editBandInfo(EditBandReq req) {

        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();
        Integer userId = user.getUserId();

        Integer bandId = req.getBandId();
        Band band = bandMapper.queryByBandId(bandId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "未查找到相关乐队信息！");
        ThrowUtil.throwIf(!band.getLeaderId().equals(userId), StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改乐队信息！");

        // 过滤简介
        String profile = req.getProfile();
        String cleanProfile = ContentUtil.cleanContent(profile);

        req.setProfile(cleanProfile);

        // 触发敏感词计数器
        sensitiveHandler.handleAccSensitive(userId, !profile.equals(cleanProfile));

        return bandMapper.editInfo(req);
    }

    /**
     * 获取乐队简略信息
     *
     * @return band brief info
     */
    public List<BandInfoVO> listBandBriefInfo() {
        // 获取已发布的乐队信息
        List<Band> bandInfoList = bandMapper.listBandInfo();

        // 获取队长姓名
        return bandInfoList.stream().map(band -> {
            String leaderName = memberMapper.queryNameByMemberId(band.getLeaderId());
            BandLike bandLike = bandLikeMapper.queryByBandIdAndUserId(band.getBandId(), SysUtil.getCurrUser().getUserId());
            return new BandInfoVO(band.getBandId(), band.getName(), band.getFoundTime(),
                    leaderName, band.getMemberNum(), bandLike != null);
        }).collect(Collectors.toList());
    }

    /**
     * 分页获取乐队简略信息
     * 每页限制为15条
     *
     * @param current 当前页码
     * @return band brief info
     */
    public Page<BandInfoVO> listBandBriefInfoByPage(Integer current) {
        LambdaQueryWrapper<Band> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Band::getIsRelease, CommonConstant.RELEASE);

        Page<Band> bandPage = bandMapper.selectPage(new Page<>(current, CommonConstant.DEFAULT_PAGE_SIZE), queryWrapper);

        return convertBandInfoVOPage(bandPage);
    }

    /**
     * 获取乐队详细信息
     *
     * @param bandId 乐队ID
     * @return band info vo
     */
    public BandDetailsVO listBandInfoVO(Integer bandId) {
        // 获取乐队信息
        BandDetailsVO bandDetailsVO = bandMapper.queryBandInfoVOByBandId(bandId, true);
        UserLoginVO currUser = SysUtil.getCurrUser();

        // TODO 拆分权限为一个新的类，每次前端不需要请求所有的信息，提高性能
        // 如果当前乐队是FAN, 就可以喜欢乐队
        if (UserRoleType.FAN.getType().equals(currUser.getType())) {
            bandDetailsVO.setCanLike(Boolean.TRUE);
            BandLike bandLike = bandLikeMapper.queryByBandIdAndUserId(bandId, currUser.getUserId());
            bandDetailsVO.setIsLiked(bandLike != null);
        }

        // 当前用户如果是Member，就可以加入乐队
        if (UserRoleType.MEMBER.getType().equals(currUser.getType())) {
            bandDetailsVO.setCanJoin(Boolean.TRUE);
            Member member = memberMapper.queryByMemberId(currUser.getUserId());
            bandDetailsVO.setIsJoined(member.getBandId() != null && bandId.equals(member.getBandId()));
        }

        return bandDetailsVO;
    }

    /**
     * 发布乐队信息
     *
     * @return true - 发布成功
     */
    public Boolean releaseBand() {
        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(user.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止该操作");

        // 事务处理
        return transactionTemplate.execute(status -> {
            try {
                albumMapper.updateAlbumReleaseStatusByBandName(band.getName(), CommonConstant.RELEASE);
                songMapper.updateReleaseStatusByBandId(band.getBandId(), CommonConstant.RELEASE);
                concertMapper.updateReleaseStatusByBandId(band.getBandId(), CommonConstant.RELEASE);
                memberMapper.updateReleaseStatusByBandId(band.getBandId(), CommonConstant.RELEASE);
                return bandMapper.updateReleaseStatusByBandId(band.getBandId(), CommonConstant.RELEASE);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    /**
     * 撤销发布乐队信息
     *
     * @return true - 撤销发布成功
     */
    public Boolean unReleaseBand() {
        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(user.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止该操作");
        // 事务处理
        return transactionTemplate.execute(status -> {
            try {
                concertMapper.updateReleaseStatusByBandId(band.getBandId(), CommonConstant.UN_RELEASE);
                memberMapper.updateReleaseStatusByBandId(band.getBandId(), CommonConstant.UN_RELEASE);
                return bandMapper.updateReleaseStatusByBandId(band.getBandId(), CommonConstant.UN_RELEASE);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    /**
     * 获取当前登录用户（队长）的乐队详细信息
     *
     * @return band info vo
     */
    public BandDetailsVO listCurrBandInfoVO() {
        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(user.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止该操作");

        return listBandInfoVO(band.getBandId());
    }

    /**
     * 查询当前乐队是否已经发布
     *
     * @return 1 - 已发布
     */
    public Integer queryCurrBandReleaseStatus() {
        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(user.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止该操作");

        return bandMapper.queryCurrBandReleaseStatus(band.getBandId());
    }


    // --------------------------
    // private util function
    // --------------------------

    private Page<BandInfoVO> convertBandInfoVOPage(Page<Band> bandPage) {
        Page<BandInfoVO> bandInfoVOPage = new Page<>(bandPage.getCurrent(), bandPage.getSize(), bandPage.getTotal(), bandPage.searchCount());

        List<BandInfoVO> bandInfoVOList = bandPage.getRecords().stream().map(band -> {
            String leaderName = memberMapper.queryNameByMemberId(band.getLeaderId());
            BandLike bandLike = bandLikeMapper.queryByBandIdAndUserId(band.getBandId(), SysUtil.getCurrUser().getUserId());
            return new BandInfoVO(band.getBandId(), band.getName(), band.getFoundTime(),
                    leaderName, band.getMemberNum(), bandLike != null);
        }).collect(Collectors.toList());
        bandInfoVOPage.setRecords(bandInfoVOList);
        return bandInfoVOPage;
    }


}
