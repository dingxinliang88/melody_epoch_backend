package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.manager.EmailManager;
import io.github.dingxinliang88.pojo.dto.email.EmailCaptchaReq;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 邮箱服务模块
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    @Resource
    private EmailManager emailManager;

    @GetMapping("/captcha")
    public BaseResponse<Boolean> genCaptcha(@Validated EmailCaptchaReq req) {
        return RespUtil.success(emailManager.genCaptcha(req));
    }
}
