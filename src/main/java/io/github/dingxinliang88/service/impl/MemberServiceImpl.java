package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.aspect.auth.LoginFunc;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.mapper.MemberMapper;
import io.github.dingxinliang88.pojo.dto.member.JoinBandReq;
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
        Band band = bandMapper.queryByBandId(bandId);
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
                memberMapper.updateBandIdAndBandName(currUser.getUserId(), bandId, band.getName());
                // 乐队人数 + 1
                return bandMapper.updateMemberNum(bandId, 1);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });

    }

}
