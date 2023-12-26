package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.po.User;
import io.github.dingxinliang88.pojo.vo.user.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User queryByUserId(Integer userId);

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

    /**
     * 根据用户id查询用户昵称
     *
     * @param userId 用户ID
     * @return 用户昵称
     */
    String queryNickNameByUserId(Integer userId);

    /**
     * 根据用户id查询用户信息
     *
     * @param userId user id
     * @return user info vo
     */
    UserInfoVO queryUserInfoByUserId(Integer userId);

    /**
     * 根据用户ID修改用户昵称
     *
     * @param userId   user id
     * @param nickname nick name
     * @return true - 修改成功
     */
    Boolean updateNickNameByUserId(Integer userId, String nickname);

    /**
     * 修改用户邮箱信息
     *
     * @param userId user id
     * @param email  email
     * @return true - 修改成功
     */
    Boolean updateEmailByUserId(Integer userId, String email);

    /**
     * 改变用户类型
     *
     * @param userId user id
     * @param type   user type
     * @return true - 修改成功
     */
    Boolean updateTypeByUserId(Integer userId, Integer type);

}
