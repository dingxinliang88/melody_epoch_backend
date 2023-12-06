package io.github.dingxinliang88.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.github.dingxinliang88.utils.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static io.github.dingxinliang88.constants.EmailConstant.CAPTCHA_KEY;

/**
 * 邮箱服务模块
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    private final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @GetMapping("/captcha")
    public Boolean getCaptcha(@RequestParam(value = "email") String emailAccount) {
        if (StrUtil.isBlank(emailAccount)) {
            logger.error("邮箱为空!");
            return Boolean.FALSE;
        }

        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-za-z0-9.-]+$";
        if (!Pattern.matches(emailPattern, emailAccount)) {
            logger.error("邮箱格式错误！");
            return Boolean.FALSE;
        }

        String captcha = RandomUtil.randomNumbers(6);

        try {
            EmailUtil.sendEmail(emailAccount, captcha);
            redisTemplate.opsForValue().set(CAPTCHA_KEY + emailAccount, captcha, 5, TimeUnit.MINUTES);
            return Boolean.TRUE;
        } catch (MessagingException e) {
            logger.error("发送验证码失败！");
            throw new RuntimeException(e);
        }
    }
}
