package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.member.JoinBandReq;
import io.github.dingxinliang88.pojo.dto.member.LeaveBandReq;
import io.github.dingxinliang88.pojo.po.Member;

import javax.servlet.http.HttpServletRequest;

/**
 * Member Service
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public interface MemberService extends IService<Member> {

    /**
     * 加入乐队
     *
     * @param req     加入乐队请求
     * @param request http request
     * @return true - 加入成功
     */
    Boolean joinBand(JoinBandReq req, HttpServletRequest request);

    /**
     * 离开乐队
     *
     * @param req     离开乐队请求
     * @param request http request
     * @return true - 离开成功
     */
    Boolean leaveBand(LeaveBandReq req, HttpServletRequest request);
}
