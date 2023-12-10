package io.github.dingxinliang88;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import io.github.dingxinliang88.utils.SysUtil;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public class MainTest {

    @Test
    public void testPwd() {
        String salt = RandomUtil.randomString(16);
        String originPassword = "admin123";
        String encryptedPassword = DigestUtil.md5Hex(salt + originPassword, StandardCharsets.UTF_8);
        System.out.println("salt = " + salt);
        System.out.println("encryptedPassword = " + encryptedPassword);
    }

    @Test
    public void genSecretKey() {
        String secretKey = RandomUtil.randomString(32);
        System.out.println("secretKey = " + secretKey);
    }

    @Test
    public void testGenCaptcha() {
        System.out.println(SysUtil.genEmailCaptcha());
    }
}
