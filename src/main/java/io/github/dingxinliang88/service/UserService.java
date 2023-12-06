package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.user.AccRegisterReq;
import io.github.dingxinliang88.pojo.dto.user.EmailLoginReq;
import io.github.dingxinliang88.pojo.po.User;

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
     * 邮箱验证码注册
     *
     * @param req     注册信息封装体
     * @param request http request
     * @return user id
     */
    Integer emailRegisterLogin(EmailLoginReq req, HttpServletRequest request);
}
