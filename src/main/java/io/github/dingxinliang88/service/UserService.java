package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.user.*;
import io.github.dingxinliang88.pojo.po.User;
import io.github.dingxinliang88.pojo.vo.user.UserInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserAuthType;

import javax.servlet.http.HttpServletRequest;

/**
 * User Service
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public interface UserService extends IService<User> {
    /**
     * 账号密码注册
     *
     * @param req     注册信息封装体
     * @param request http request
     * @return user id
     */
    Integer userAccRegister(AccRegisterReq req, HttpServletRequest request);


    /**
     * 邮箱密码验证码注册
     *
     * @param req     注册信息封装体
     * @param request http request
     * @return user id
     */
    Integer userEmailRegister(EmailRegisterReq req, HttpServletRequest request);

    /**
     * 邮箱验证码登录
     *
     * @param req     登录信息封装体
     * @param request http request
     * @return user info
     */
    String userEmailLogin(EmailLoginReq req, HttpServletRequest request);

    /**
     * 账号密码登录
     *
     * @param req     登录信息封装体
     * @param request http request
     * @return user info
     */
    String userAccLogin(AccLoginReq req, HttpServletRequest request);

    /**
     * 获取当前登录对象
     *
     * @param request http request
     * @return current login user info
     */
    UserInfoVO getCurrUser(HttpServletRequest request);

    /**
     * 用户登出
     *
     * @param request http request
     * @return true - 登出成功
     */
    Boolean userLogout(HttpServletRequest request);

    /**
     * 获取当前登录用户的角色（队长、成员、乐迷、管理员
     *
     * @param request http request
     * @return user type vo
     */
    UserAuthType getUserAuthType(HttpServletRequest request);

    /**
     * 修改用户信息
     *
     * @param req     修改用户请求
     * @param request http request
     * @return true - 修改成功
     */
    Boolean editUserInfo(EditUserReq req, HttpServletRequest request);

    /**
     * 用户绑定验证码
     *
     * @param req     绑定邮箱请求
     * @param request http request
     * @return true - 绑定成功
     */
    Boolean bindEmail(BindEmailReq req, HttpServletRequest request);
}
