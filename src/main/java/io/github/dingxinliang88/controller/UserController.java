package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.user.AccLoginReq;
import io.github.dingxinliang88.pojo.dto.user.AccRegisterReq;
import io.github.dingxinliang88.pojo.dto.user.EmailLoginReq;
import io.github.dingxinliang88.pojo.vo.UserLoginVO;
import io.github.dingxinliang88.service.UserService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<UserLoginVO> userEmailLogin(@RequestBody @Validated EmailLoginReq req, HttpServletRequest request) {
        return RespUtil.success(userService.userEmailLogin(req, request));
    }

    @PostMapping("/acc_login")
    public BaseResponse<UserLoginVO> userAccLogin(@RequestBody @Validated AccLoginReq req, HttpServletRequest request) {
        return RespUtil.success(userService.userAccLogin(req, request));
    }

    @GetMapping("/curr")
    public BaseResponse<UserLoginVO> getCurrUser(HttpServletRequest request) {
        return RespUtil.success(userService.getCurrUser(request));
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        return RespUtil.success(userService.userLogout(request));
    }

}
