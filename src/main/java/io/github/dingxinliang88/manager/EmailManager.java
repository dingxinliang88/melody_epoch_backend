package io.github.dingxinliang88.manager;

import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.exception.BizException;
import io.github.dingxinliang88.pojo.dto.email.EmailCaptchaReq;
import io.github.dingxinliang88.utils.EmailUtil;
import io.github.dingxinliang88.utils.SysUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.concurrent.TimeUnit;

import static io.github.dingxinliang88.constants.EmailConstant.CAPTCHA_KEY;

/**
 * 邮件管理
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Component
public class EmailManager {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public Boolean genCaptcha(EmailCaptchaReq req) {
        String email = req.getEmail();
        String captcha = SysUtil.genEmailCaptcha();

        try {
            EmailUtil.sendEmail(email, captcha);
            redisTemplate.opsForValue().set(CAPTCHA_KEY + email, captcha, 5, TimeUnit.MINUTES);
            return Boolean.TRUE;
        } catch (MessagingException e) {
            throw new BizException(StatusCode.SYSTEM_ERROR, e.getMessage());
        }
    }
}
