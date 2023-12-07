package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.po.Member;
import org.apache.ibatis.annotations.Mapper;

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
     * 修改成员所在乐队信息
     *
     * @param memberId 成员ID
     * @param bandId   队伍ID
     * @param bandName 队伍名称
     * @return true - 修改成功
     */
    Boolean updateBandIdAndBandName(Integer memberId, Integer bandId, String bandName);
}
