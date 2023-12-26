package io.github.dingxinliang88.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dingxinliang88.aspect.auth.AuthCheck;
import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.user.AccRegisterReq;
import io.github.dingxinliang88.pojo.dto.user.BanUserReq;
import io.github.dingxinliang88.pojo.vo.user.UserInfoVO;
import io.github.dingxinliang88.service.UserService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 管理员模块
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private UserService userService;

    @PostMapping("/user/add")
    @AuthCheck(role = "admin")
    public BaseResponse<Integer> createUser(@RequestBody @Validated AccRegisterReq req) {
        return RespUtil.success(userService.userAccRegister(req));
    }

    @PostMapping("/user/ban")
    @AuthCheck(role = "admin")
    public BaseResponse<Boolean> banUser(@RequestBody @Validated BanUserReq req) {
        return RespUtil.success(userService.banUser(req));
    }

    @PostMapping("/user/unban")
    @AuthCheck(role = "admin")
    public BaseResponse<Boolean> unbanUser(@RequestBody @Validated BanUserReq req) {
        return RespUtil.success(userService.unbanUser(req));
    }

    @GetMapping("/page/user")
    @AuthCheck(role = "admin")
    public BaseResponse<Page<UserInfoVO>> getUserInfoVOByPage(@RequestParam(value = "curr") @NotNull @Min(1) Integer current,
                                                              @RequestParam(value = "size") @NotNull @Min(5) Integer size) {
        return RespUtil.success(userService.getUserInfoVOByPage(current, size));
    }
}
