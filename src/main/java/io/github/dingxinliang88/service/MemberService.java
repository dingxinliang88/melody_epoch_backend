package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.member.EditMemberReq;
import io.github.dingxinliang88.pojo.dto.member.EditPartReq;
import io.github.dingxinliang88.pojo.dto.member.JoinBandReq;
import io.github.dingxinliang88.pojo.po.Member;
import io.github.dingxinliang88.pojo.vo.member.MemberInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    Boolean leaveBand(JoinBandReq req, HttpServletRequest request);

    /**
     * 成员修改自己的信息
     *
     * @param req     修改信息请求
     * @param request http request
     * @return true - 修改成功
     */
    Boolean editInfo(EditMemberReq req, HttpServletRequest request);

    /**
     * 修改乐队成员分工（仅队长）
     *
     * @param req     修改分工请求
     * @param request http request
     * @return true - 修改成功
     */
    Boolean editMemberPart(EditPartReq req, HttpServletRequest request);

    /**
     * 查询所有乐队成员信息（包括未加入乐队的）
     *
     * @param request http request
     * @return member info vo list
     */
    List<MemberInfoVO> listMembers(HttpServletRequest request);

    /**
     * 获取当前登录用户（队长）所在的乐队的所有乐队成员信息
     *
     * @param request http request
     * @return member info vo list
     */
    List<MemberInfoVO> listMemberInCurrBand(HttpServletRequest request);
}
