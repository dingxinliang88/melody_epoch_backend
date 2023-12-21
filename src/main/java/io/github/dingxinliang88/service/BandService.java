package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.exception.BizException;
import io.github.dingxinliang88.mapper.*;
import io.github.dingxinliang88.pojo.dto.band.AddBandReq;
import io.github.dingxinliang88.pojo.dto.band.EditBandReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.*;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import io.github.dingxinliang88.pojo.vo.band.BandDetailsVO;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;
import io.github.dingxinliang88.pojo.vo.concert.ConcertInfoVO;
import io.github.dingxinliang88.pojo.vo.member.MemberInfoVO;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
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
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
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
    private TransactionTemplate transactionTemplate;

    /**
     * 创建乐队
     *
     * @param req     创建乐队请求
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
     * @param req     修改乐队信息请求
     * @return true - 修改成功
     */
    public Boolean editInfo(EditBandReq req) {

        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Integer bandId = req.getBandId();
        Band band = bandMapper.queryByBandId(bandId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "未查找到相关乐队信息！");
        ThrowUtil.throwIf(!band.getLeaderId().equals(user.getUserId()), StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改乐队信息！");

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
     * 获取乐队详细信息
     *
     * @param bandId  乐队ID
     * @return band info vo
     */
    public BandDetailsVO listBandInfoVO(Integer bandId) {
        // 获取乐队信息
        BandDetailsVO bandDetailsVO = bandMapper.queryBandInfoVOByBandId(bandId, true);
        BandLike bandLike = bandLikeMapper.queryByBandIdAndUserId(bandId, SysUtil.getCurrUser().getUserId());
        bandDetailsVO.setIsLiked(bandLike != null);
        // 获取乐队成员信息
        List<MemberInfoVO> members = memberMapper.queryMembersByBandId(bandId);
        bandDetailsVO.setMembers(members);
        // 获取专辑信息
        List<AlbumInfoVO> albums = albumMapper.queryAlbumByBandName(bandDetailsVO.getName());
        bandDetailsVO.setAlbums(albums);
        // 获取歌曲信息
        List<SongInfoVO> songs = songMapper.querySongsByBandId(bandId);
        bandDetailsVO.setSongs(songs);
        // 获取演唱会信息
        List<ConcertInfoVO> concerts = concertMapper.queryConcertByBandId(bandId);
        bandDetailsVO.setConcerts(concerts);

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


}
