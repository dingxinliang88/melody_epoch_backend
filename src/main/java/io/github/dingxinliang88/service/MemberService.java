package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.mapper.MemberMapper;
import io.github.dingxinliang88.pojo.dto.member.EditPartReq;
import io.github.dingxinliang88.pojo.dto.member.JoinBandReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Band;

import io.github.dingxinliang88.pojo.po.Member;
import io.github.dingxinliang88.pojo.vo.member.MemberInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Band Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Service
public class MemberService extends ServiceImpl<MemberMapper, Member> {

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private BandMapper bandMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * 加入乐队
     *
     * @param req 加入乐队请求
     * @return true - 加入成功
     */
    public Boolean joinBand(JoinBandReq req) {
        final Integer bandId = req.getBandId();

        // 查找Band是否存在
        Band band = bandMapper.queryByBandId(bandId, false);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "乐队不存在！");

        // 当前用户是否已经加入了乐队
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.MEMBER.getType().equals(currUser.getType()),
                StatusCode.NO_AUTH_ERROR, "无权加入乐队！");

        Member member = memberMapper.queryByMemberId(currUser.getUserId());
        ThrowUtil.throwIf(member.getBandId() != null, StatusCode.NO_AUTH_ERROR, "您已加入乐队！");

        return transactionTemplate.execute(status -> {
            try {
                // 修改当前member所在乐队信息
                memberMapper.updateBandIdAndBandName(currUser.getUserId(), bandId, band.getName(),
                        LocalDateTime.now(), null, band.getIsRelease());
                // 乐队人数 + 1
                return bandMapper.updateMemberNum(bandId, 1);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });

    }

    /**
     * 离开乐队
     *
     * @param req 离开乐队请求
     * @return true - 离开成功
     */
    public Boolean leaveBand(JoinBandReq req) {
        final Integer bandId = req.getBandId();

        // 查询Band是否存在
        Band band = bandMapper.queryByBandId(bandId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "乐队不存在！");

        // 当前用户是否加入了该乐队
        UserLoginVO currUser = SysUtil.getCurrUser();
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

    /**
     * 修改乐队成员分工（仅队长）
     *
     * @param req 修改分工请求
     * @return true - 修改成功
     */
    public Boolean editMemberPart(EditPartReq req) {
        // 判断当前登录用户是否是队长
        Integer memberId = req.getMemberId();
        String part = req.getPart();

        UserLoginVO currUser = SysUtil.getCurrUser();
        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止修改");

        return memberMapper.editMemberPart(memberId, part);
    }

    /**
     * 查询所有乐队成员信息（包括未加入乐队的）
     *
     * @return member info vo list
     */
    public List<MemberInfoVO> listMemberInfoVO() {
        List<Member> members = memberMapper.listMembers();
        return members.stream().map(member -> {
            MemberInfoVO memberInfoVO = new MemberInfoVO(member);
            if (CommonConstant.UN_RELEASE.equals(member.getIsRelease())) {
                memberInfoVO.setPart("-");
                memberInfoVO.setBandName("-");
                memberInfoVO.setJoinTime(null);
                memberInfoVO.setLeaveTime(null);
            }
            return memberInfoVO;
        }).collect(Collectors.toList());
    }

    /**
     * 查询所有乐队成员信息（包括未加入乐队的）
     *
     * @return member info vo list
     */
    public Page<MemberInfoVO> listMemberInfoVOByPage(Integer current) {
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        Page<Member> memberPage = memberMapper.selectPage(new Page<>(current, CommonConstant.DEFAULT_PAGE_SIZE), queryWrapper);

        return convertMemberInfoVOPage(memberPage, false);
    }


    /**
     * 获取当前登录用户（队长）所在的乐队的所有乐队成员信息
     *
     * @return member info vo list
     */
    public List<MemberInfoVO> listMemberInCurrBand() {
        // 获取当前登录用户，队长的操作
        UserLoginVO currUser = SysUtil.getCurrUser();
        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止的操作！");

        return memberMapper.listMemberInfoVO(band.getBandId());
    }

    /**
     * 分页获取当前登录用户（队长）所在的乐队的所有乐队成员信息
     *
     * @return member info vo list
     */
    public Page<MemberInfoVO> listMemberInCurrBandByPage(Integer current, Integer size) {
        // 获取当前登录用户，队长的操作
        UserLoginVO currUser = SysUtil.getCurrUser();
        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止的操作！");

        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Member::getBandId, band.getBandId());
        Page<Member> memberPage = memberMapper.selectPage(new Page<>(current, size), queryWrapper);
        return convertMemberInfoVOPage(memberPage, true);
    }

    /**
     * 分页获取指定乐队的所有乐队成员信息
     *
     * @return member info vo list
     */
    public Page<MemberInfoVO> listMemberInBandByPage(Integer bandId, Integer current, Integer size) {
        Band band = bandMapper.queryByBandId(bandId, false);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "查询无果");

        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Member::getBandId, band.getBandId());
        Page<Member> memberPage = memberMapper.selectPage(new Page<>(current, size), queryWrapper);
        return convertMemberInfoVOPage(memberPage, true);
    }

    // ------------------------
    // private util function
    // ------------------------
    private Boolean handleBandLeaderLeave(final Member member, final Integer bandId) {
        return transactionTemplate.execute(status -> {
            try {
                // 加入乐队第二早的乐队成员
                Member secondaryMember = memberMapper.querySecondaryMember(bandId);
                // 修改当前member所在乐队信息（离开时间为当前时间，乐队ID、乐队名称置空）
                memberMapper.updateBandIdAndBandName(member.getMemberId(), null, null,
                        member.getJoinTime(), LocalDateTime.now(), CommonConstant.UN_RELEASE);
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
                        member.getJoinTime(), LocalDateTime.now(), CommonConstant.UN_RELEASE);
                // 乐队人数 - 1
                return bandMapper.updateMemberNum(bandId, -1);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    private Page<MemberInfoVO> convertMemberInfoVOPage(Page<Member> memberPage, boolean curr) {
        Page<MemberInfoVO> memberInfoVOPage = new Page<>(memberPage.getCurrent(), memberPage.getSize(), memberPage.getTotal(), memberPage.searchCount());

        List<MemberInfoVO> memberInfoVOList = memberPage.getRecords().stream().map(member -> {
            MemberInfoVO memberInfoVO = new MemberInfoVO(member);
            if (!curr && CommonConstant.UN_RELEASE.equals(member.getIsRelease())) {
                memberInfoVO.setPart("-");
                memberInfoVO.setBandName("-");
                memberInfoVO.setJoinTime(null);
                memberInfoVO.setLeaveTime(null);
            }
            return memberInfoVO;
        }).collect(Collectors.toList());
        memberInfoVOPage.setRecords(memberInfoVOList);
        return memberInfoVOPage;

    }


}
