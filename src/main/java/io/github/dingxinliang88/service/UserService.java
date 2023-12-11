package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.user.AccLoginReq;
import io.github.dingxinliang88.pojo.dto.user.AccRegisterReq;
import io.github.dingxinliang88.pojo.dto.user.EmailLoginReq;
import io.github.dingxinliang88.pojo.dto.user.EmailRegisterReq;
import io.github.dingxinliang88.pojo.po.User;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;

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
    UserLoginVO getCurrUser(HttpServletRequest request);

    /**
     * 用户登出
     *
     * @param request http request
     * @return true - 登出成功
     */
    Boolean userLogout(HttpServletRequest request);

}
