package io.github.dingxinliang88.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import io.github.dingxinliang88.pojo.vo.UserLoginVO;

import java.nio.charset.StandardCharsets;

import static io.github.dingxinliang88.constants.EmailConstant.CAPTCHA_LEN;
import static io.github.dingxinliang88.constants.UserConstant.*;

/**
 * 系统工具类
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public class SysUtil {

    public static String genUserNickName() {
        return DEFAULT_NICK_NAME_PREFIX + RandomUtil.randomString(NICK_NAME_LEN);
    }

    public static String genUserPwdSalt() {
        return genSalt(SALT_LEN);
    }

    public static String genSalt(int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("len 必须大于0");
        }
        return RandomUtil.randomString(len);
    }

    public static String genEmailCaptcha() {
        return genCaptcha(CAPTCHA_LEN);
    }

    public static String genCaptcha(int len) {
        return RandomUtil.randomNumbers(len);
    }

    public static String encryptedPwd(String salt, String originPwd) {
        return DigestUtil.md5Hex(salt + originPwd, StandardCharsets.UTF_8);
    }

    public static UserLoginVO getCurrUser() {
        return UserHolder.getUser();
    }
}
