package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.aspect.auth.LoginFunc;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.mapper.MemberMapper;
import io.github.dingxinliang88.pojo.dto.member.EditInfoReq;
import io.github.dingxinliang88.pojo.dto.member.JoinBandReq;
import io.github.dingxinliang88.pojo.dto.member.LeaveBandReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.Member;
import io.github.dingxinliang88.pojo.vo.UserLoginVO;
import io.github.dingxinliang88.service.MemberService;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Band Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member>
        implements MemberService {

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private BandMapper bandMapper;

    @Resource
    private TransactionTemplate transactionTemplate;


    @Override
    @LoginFunc
    public Boolean joinBand(JoinBandReq req, HttpServletRequest request) {
        final Integer bandId = req.getBandId();

        // 查找Band是否存在
        Band band = bandMapper.queryByBandIdInner(bandId);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "乐队不存在！");

        // 当前用户是否已经加入了乐队
        UserLoginVO currUser = SysUtil.getCurrUser(request);
        ThrowUtil.throwIf(!UserRoleType.MEMBER.getType().equals(currUser.getType()),
                StatusCode.NO_AUTH_ERROR, "无权加入乐队！");

        Member member = memberMapper.queryByMemberId(currUser.getUserId());
        ThrowUtil.throwIf(member.getBandId() != null, StatusCode.NO_AUTH_ERROR, "您已加入乐队！");

        return transactionTemplate.execute(status -> {
            try {
                // 修改当前member所在乐队信息
                memberMapper.updateBandIdAndBandName(currUser.getUserId(), bandId, band.getName(),
                        LocalDateTime.now(), null);
                // 乐队人数 + 1
                return bandMapper.updateMemberNum(bandId, 1);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });

    }

    @Override
    public Boolean leaveBand(LeaveBandReq req, HttpServletRequest request) {
        final Integer bandId = req.getBandId();

        // 查询Band是否存在
        Band band = bandMapper.queryByBandIdInner(bandId);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "乐队不存在！");

        // 当前用户是否加入了该乐队
        UserLoginVO currUser = SysUtil.getCurrUser(request);
        ThrowUtil.throwIf(!UserRoleType.MEMBER.getType().equals(currUser.getType()),
                StatusCode.NO_AUTH_ERROR, "无权离开乐队！");
        Member member = memberMapper.queryByMemberIdAndBandId(currUser.getUserId(), bandId);
        ThrowUtil.throwIf(member == null, StatusCode.NOT_FOUND_ERROR, "未查找到加入乐队的信息！");

        // 特判：当前用户是队长
        if (band.getLeaderId().equals(member.getMemberId())) {
            return handleBandLeaderLeave(member, bandId);
        }

        // 不是队长，更新乐队信息和member信息
        return handleRegularLeave(member, bandId);
    }

    @Override
    @LoginFunc
    public Boolean editInfo(EditInfoReq req, HttpServletRequest request) {

        Integer memberId = req.getMemberId();

        // 检查是否是本人
        UserLoginVO currUser = SysUtil.getCurrUser(request);
        ThrowUtil.throwIf(!currUser.getUserId().equals(memberId), StatusCode.NO_AUTH_ERROR,
                "无权修改其他成员信息！");

        // 查找相关的成员是否存在
        Member member = memberMapper.queryByMemberId(memberId);
        ThrowUtil.throwIf(member == null, StatusCode.NOT_FOUND_ERROR, "未查找到相关成员信息！");


        // 更新相关的信息
        return memberMapper.updateInfo(req);
    }


    private Boolean handleBandLeaderLeave(final Member member, final Integer bandId) {
        return transactionTemplate.execute(status -> {
            try {
                // 加入乐队第二早的乐队成员
                Member secondaryMember = memberMapper.querySecondaryMember(bandId);
                // 修改当前member所在乐队信息（离开时间为当前时间，乐队ID、乐队名称置空）
                memberMapper.updateBandIdAndBandName(member.getMemberId(), null, null,
                        member.getJoinTime(), LocalDateTime.now());
                if (secondaryMember == null) {
                    // 解散队伍
                    return bandMapper.disband(bandId);
                } else {
                    // 队长位置顺位给加入乐队第二早的成员
                    bandMapper.updateLeaderId(bandId, secondaryMember.getMemberId());
                    // 乐队人数 - 1
                    return bandMapper.updateMemberNum(bandId, -1);
                }
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    private Boolean handleRegularLeave(final Member member, final Integer bandId) {
        return transactionTemplate.execute(status -> {
            try {
                // 修改当前member所在乐队信息（离开时间为当前时间，乐队ID、乐队名称置空）
                memberMapper.updateBandIdAndBandName(member.getMemberId(), null, null,
                        member.getJoinTime(), LocalDateTime.now());
                // 乐队人数 - 1
                return bandMapper.updateMemberNum(bandId, -1);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

}
