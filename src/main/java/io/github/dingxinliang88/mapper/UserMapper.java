package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据邮箱查询用户信息
     *
     * @param email 邮箱信息
     * @return 用户信息
     */
    User queryByEmail(String email);


    /**
     * 根据账号查询用户信息
     *
     * @param account 账号
     * @return 用户信息
     */
    User queryByAccount(String account);

}
