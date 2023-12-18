package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.exception.BizException;
import io.github.dingxinliang88.mapper.*;
import io.github.dingxinliang88.pojo.dto.band.AddBandReq;
import io.github.dingxinliang88.pojo.dto.band.EditBandReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.*;
import io.github.dingxinliang88.pojo.vo.band.BandDetailsVO;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.service.BandService;
import io.github.dingxinliang88.utils.RedisUtil;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
public class BandServiceImpl extends ServiceImpl<BandMapper, Band>
        implements BandService {

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

    @Override
    public Integer addBand(AddBandReq req, HttpServletRequest request) {
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

        // todo 判断当前bandName是否已经被注册了

        // 判断当前leaderId是否已经加入了队伍
        Band bandFromDB = bandMapper.queryByLeaderId(leaderId, true);
        ThrowUtil.throwIf(bandFromDB != null, StatusCode.DUPLICATE_DATA, "该队长已经加入乐队了！");

        final Integer leader = leaderId;
        return transactionTemplate.execute(status -> {
            try {
                Band band = new Band(bandName, leader, profile, LocalDateTime.now());
                band.setMemberNum(1);
                bandMapper.insert(band);
                // 修改对应的member信息，band_id、band_name
                memberMapper.updateBandIdAndBandName(leader, band.getBandId(), band.getName(),
                        LocalDateTime.now(), null, band.getIsRelease());
                // 删除leader的权限缓存
                redisUtil.delete(USER_AUTH_TYPE_PREFIX + leader);
                return band.getBandId();
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    @Override
    public Boolean editInfo(EditBandReq req, HttpServletRequest request) {

        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Integer bandId = req.getBandId();
        Band band = bandMapper.queryByBandId(bandId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "未查找到相关乐队信息！");
        ThrowUtil.throwIf(!band.getLeaderId().equals(user.getUserId()), StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改乐队信息！");

        return bandMapper.editInfo(req);
    }

    @Override
    public List<BandInfoVO> listBandBriefInfo(HttpServletRequest request) {
        // 获取已发布的乐队信息
        List<Band> bandInfoList = bandMapper.listBandInfo();

        // 获取队长姓名
        return bandInfoList.stream().map(band -> {
            String leaderName = memberMapper.queryNameByMemberId(band.getLeaderId());
            BandLike bandLike = bandLikeMapper.queryByBandIdAndUserId(band.getBandId(), SysUtil.getCurrUser().getUserId());
            return BandInfoVO
                    .builder()
                    .bandId(band.getBandId())
                    .leaderName(leaderName)
                    .name(band.getName())
                    .isLiked(bandLike != null)
                    .foundTime(band.getFoundTime())
                    .memberNum(band.getMemberNum())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public BandDetailsVO listBandInfoVO(Integer bandId, HttpServletRequest request) {
        // 获取乐队信息
        BandDetailsVO bandDetailsVO = bandMapper.queryBandInfoVOByBandId(bandId);
        BandLike bandLike = bandLikeMapper.queryByBandIdAndUserId(bandId, SysUtil.getCurrUser().getUserId());
        bandDetailsVO.setIsLiked(bandLike != null);
        // 获取乐队成员信息
        List<Member> members = memberMapper.queryMembersByBandId(bandId);
        bandDetailsVO.setMembers(members);
        // 获取专辑信息
        List<Album> albums = albumMapper.queryAlbumByBandName(bandDetailsVO.getName());
        bandDetailsVO.setAlbums(albums);
        // 获取歌曲信息
        List<Song> songs = songMapper.querySongsByBandId(bandId);
        bandDetailsVO.setSongs(songs);
        // 获取演唱会信息
        List<Concert> concerts = concertMapper.queryConcertByBandId(bandId);
        bandDetailsVO.setConcerts(concerts);

        return bandDetailsVO;
    }

    @Override
    public Boolean releaseBand(HttpServletRequest request) {
        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(user.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止该操作");

        // 事务处理
        return transactionTemplate.execute(status -> {
            try {
                // TODO 起一个异步线程去更新专辑发行时间
                albumMapper.releaseAlbumInfo(band.getName());
                songMapper.releaseSongInfo(band.getBandId());
                concertMapper.releaseConcertInfo(band.getBandId());
                memberMapper.releaseMemberInfo(band.getBandId());
                return bandMapper.releaseBandInfo(band.getBandId());
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    @Override
    public Boolean unReleaseBand(HttpServletRequest request) {
        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(user.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止该操作");
        // 事务处理
        return transactionTemplate.execute(status -> {
            try {
                concertMapper.unReleaseConcertInfo(band.getBandId());
                memberMapper.unReleaseMemberInfo(band.getBandId());
                return bandMapper.unReleaseBandInfo(band.getBandId());
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    @Override
    public BandDetailsVO listCurrBandInfoVO(HttpServletRequest request) {
        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(user.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止该操作");

        return listBandInfoVO(band.getBandId(), request);
    }

    @Override
    public Integer queryCurrBandReleaseStatus(HttpServletRequest request) {
        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(user.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止该操作");

        return bandMapper.queryCurrBandReleaseStatus(band.getBandId());
    }


}
