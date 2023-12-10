package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.dto.member.EditMemberReq;
import io.github.dingxinliang88.pojo.po.Member;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
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
     * @return true - 修改成功
     */
    Boolean updateBandIdAndBandName(Integer memberId, Integer bandId, String bandName, LocalDateTime joinTime, LocalDateTime leaveTime);


    /**
     * 修改个人信息
     *
     * @param req 个人信息
     * @return true - 修改成功
     */
    Boolean updateInfo(EditMemberReq req);

    /**
     * 修改乐队成员分工
     *
     * @param memberId 成员ID
     * @param part     成员分工
     * @return true - 修改成功
     */
    Boolean editMemberPart(Integer memberId, String part);
}
