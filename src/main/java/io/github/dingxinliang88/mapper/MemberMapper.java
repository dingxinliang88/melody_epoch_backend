package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.dto.member.EditMemberReq;
import io.github.dingxinliang88.pojo.po.Member;
import io.github.dingxinliang88.pojo.vo.member.MemberInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {

    /**
     * 根据成员ID查询成员信息
     *
     * @param memberId 成员ID
     * @return 成员信息
     */
    Member queryByMemberId(Integer memberId);

    /**
     * 根据成员ID查询成员姓名
     *
     * @param memberId 成员ID
     * @return 成员姓名
     */
    String queryNameByMemberId(Integer memberId);

    /**
     * 根据成员ID和乐队ID查询成员信息
     *
     * @param memberId 成员ID
     * @param bandId   乐队ID
     * @return 成员信息
     */
    Member queryByMemberIdAndBandId(Integer memberId, Integer bandId);

    /**
     * 查找第二早加入的乐队成员
     *
     * @param bandId 乐队ID
     * @return 成员信息
     */
    Member querySecondaryMember(Integer bandId);

    /**
     * 修改成员所在乐队信息
     *
     * @param memberId  成员ID
     * @param bandId    队伍ID
     * @param bandName  队伍名称
     * @param joinTime  加入队伍时间
     * @param leaveTime 离开队伍时间
     * @param isRelease 所在乐队信息是否已经发布
     * @return true - 修改成功
     */
    Boolean updateBandIdAndBandName(Integer memberId, Integer bandId, String bandName, LocalDateTime joinTime, LocalDateTime leaveTime, Integer isRelease);

    /**
     * 修改乐队成员分工
     *
     * @param memberId 成员ID
     * @param part     成员分工
     * @return true - 修改成功
     */
    Boolean editMemberPart(Integer memberId, String part);

    /**
     * 根据乐队ID获取乐队成员信息
     *
     * @param bandId 乐队ID
     * @return members
     */
    List<MemberInfoVO> queryMembersByBandId(Integer bandId);

    /**
     * 根据乐队ID获取乐队成员信息VO
     *
     * @param bandId 乐队ID
     * @return member info vo list
     */
    List<MemberInfoVO> listMemberInfoVO(Integer bandId);

    /**
     * 查询出所有的乐队成员信息
     *
     * @return member info list
     */
    List<Member> listMembers();

    /**
     * 修改成员发布状态
     *
     * @param bandId  band id
     * @param release release status
     * @return true - 修改成功
     */
    Boolean updateReleaseStatusByBandId(Integer bandId, Integer release);

    /**
     * 撤销发布成员信息
     *
     * @param bandId band id
     */
    Boolean unReleaseMemberInfo(Integer bandId);

    /**
     * 根据成员ID查询成员信息
     *
     * @param memberId 成员id
     * @return member info vo
     */
    MemberInfoVO queryMemberInfoByMemberId(Integer memberId);

    /**
     * 修改成员信息
     *
     * @param editMemberReq 修改请求
     * @return true - 修改成功
     */
    Boolean updateInfoByMemberId(EditMemberReq editMemberReq);
}
