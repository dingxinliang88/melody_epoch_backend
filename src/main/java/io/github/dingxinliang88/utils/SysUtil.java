package io.github.dingxinliang88.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import io.github.dingxinliang88.pojo.po.User;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;

import static io.github.dingxinliang88.constants.CommonConstant.*;
import static io.github.dingxinliang88.constants.EmailConstant.CAPTCHA_LEN;
import static io.github.dingxinliang88.constants.UserConstant.*;

/**
 * 系统工具类
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
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
        return DigestUtil.bcrypt(salt + originPwd);
    }

    public static boolean checkPwd(User user, String inputPwd) {
        return DigestUtil.bcryptCheck(user.getSalt() + inputPwd, user.getPassword());
    }

    public static UserLoginVO getCurrUser() {
        return UserHolder.getUser();
    }

    /**
     * @see io.github.dingxinliang88.pojo.enums.UserRoleType
     */
    public static boolean isBanned() {
        return (UserHolder.getUser().getType() & BANNED) != 0;
    }

    /**
     * @see io.github.dingxinliang88.pojo.enums.UserRoleType
     */
    public static boolean isBanned(User user) {
        return (user.getType() & BANNED) != 0;
    }

    public static Integer genBannedType(Integer type) {
        return type | BANNED;
    }

    public static Integer genUnbannedType(Integer type) {
        return type & UNBANNED;
    }
}
