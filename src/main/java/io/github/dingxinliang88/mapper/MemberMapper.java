package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.Member;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {

}
