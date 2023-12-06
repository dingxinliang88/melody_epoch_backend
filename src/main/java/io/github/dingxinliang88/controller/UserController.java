package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.user.AccRegisterReq;
import io.github.dingxinliang88.pojo.dto.user.EmailLoginReq;
import io.github.dingxinliang88.service.UserService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户模块
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/acc_reg")
    public BaseResponse<Integer> userAccRegister(@RequestBody @Validated AccRegisterReq req, HttpServletRequest request) {
        return RespUtil.success(userService.userAccRegister(req, request));
    }

    @PostMapping("/email_login")
    public BaseResponse<Integer> userEmailLogin(@RequestBody @Validated EmailLoginReq req, HttpServletRequest request) {
        return RespUtil.success(userService.emailRegisterLogin(req, request));
    }


}
