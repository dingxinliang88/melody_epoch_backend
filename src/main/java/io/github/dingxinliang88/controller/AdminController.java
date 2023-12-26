package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.user.AccRegisterReq;
import io.github.dingxinliang88.service.UserService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 管理员模块
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private UserService userService;

    @PostMapping("/user/add")
    public BaseResponse<Integer> createUser(@RequestBody AccRegisterReq req) {
        return RespUtil.success(userService.userAccRegister(req));
    }

}
