package io.github.dingxinliang88.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;

import java.nio.charset.StandardCharsets;

import static io.github.dingxinliang88.constants.UserConstant.SALT_LEN;

/**
 * 系统工具类
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public class SysUtil {


    public static String genUserPwdSalt() {
        return genSalt(SALT_LEN);
    }

    public static String genSalt(int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("len 必须大于0");
        }
        return RandomUtil.randomString(len);
    }

    public static String encryptedPwd(String salt, String originPwd) {
        return DigestUtil.md5Hex(salt + originPwd, StandardCharsets.UTF_8);
    }
}
