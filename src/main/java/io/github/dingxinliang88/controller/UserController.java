package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.QueryReq;
import io.github.dingxinliang88.pojo.dto.user.*;
import io.github.dingxinliang88.pojo.vo.SearchVO;
import io.github.dingxinliang88.pojo.vo.user.UserAuthType;
import io.github.dingxinliang88.pojo.vo.user.UserInfoVO;
import io.github.dingxinliang88.service.UserService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户模块
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/acc_reg")
    public BaseResponse<Integer> userAccRegister(@RequestBody @Validated AccRegisterReq req) {
        return RespUtil.success(userService.userAccRegister(req));
    }

    @PostMapping("/email_reg")
    public BaseResponse<Integer> userEmailLogin(@RequestBody @Validated EmailRegisterReq req) {
        return RespUtil.success(userService.userEmailRegister(req));
    }

    @PostMapping("/email_login")
    public BaseResponse<String> userEmailLogin(@RequestBody @Validated EmailLoginReq req) {
        return RespUtil.success(userService.userEmailLogin(req));
    }

    @PostMapping("/acc_login")
    public BaseResponse<String> userAccLogin(@RequestBody @Validated AccLoginReq req) {
        return RespUtil.success(userService.userAccLogin(req));
    }

    @GetMapping("/auth")
    public BaseResponse<UserAuthType> getUserAuthType() {
        return RespUtil.success(userService.getUserAuthType());
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {
        return RespUtil.success(userService.userLogout());
    }

    @GetMapping("/curr")
    public BaseResponse<UserInfoVO> getCurrUser() {
        return RespUtil.success(userService.getCurrUser());
    }

    @PutMapping("/edit")
    public BaseResponse<Boolean> editUserInfo(@RequestBody @Validated EditUserReq req) {
        return RespUtil.success(userService.editUserInfo(req));
    }

    @PostMapping("/bind/email")
    public BaseResponse<Boolean> bindEmail(@RequestBody @Validated BindEmailReq req) {
        return RespUtil.success(userService.bindEmail(req));
    }

    @PostMapping("/query")
    public BaseResponse<SearchVO> queryInfo(@RequestBody QueryReq req) {
        return RespUtil.success(userService.queryInfo(req));
    }

}
