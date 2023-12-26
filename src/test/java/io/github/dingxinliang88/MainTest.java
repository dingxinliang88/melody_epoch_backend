package io.github.dingxinliang88;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import io.github.dingxinliang88.utils.ContentUtil;
import io.github.dingxinliang88.utils.SysUtil;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
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
    public void testBcrypt() {
        String salt = "xrncupiydjsaot8F";
        String originPassword = "member007";
        String encryptedPassword = DigestUtil.bcrypt(salt + originPassword);
        System.out.println("encryptedPassword = " + encryptedPassword);
        System.out.println(encryptedPassword.length());
        System.out.println(DigestUtil.bcryptCheck(salt + originPassword, encryptedPassword));
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

    @Test
    public void testStrUtil() {
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        System.out.println(StrUtil.join(",", ids));
    }

    @Test
    public void testLocalDateTime() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 12, 10, 10, 10);
        LocalDateTime end = LocalDateTime.of(2023, 10, 12, 11, 10, 10);
        System.out.println(start.plusHours(2).isAfter(end));
    }

    @Test
    public void testBanned() {
        int type = 0b101;
        System.out.println(type);
        System.out.println(SysUtil.genUnbannedType(type));
    }

    @Test
    public void testSensitive() {
        String originContent = "Hutool是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，傻逼、蠢蛋吧你，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。";
        String s = ContentUtil.cleanContent(originContent);
        System.out.println(s);
    }


    @Test
    public void testSet() {
        Set<Object> set = CollectionUtil.newHashSet(1, 2, 3);
        Set<Integer> integers = convertToIntegerSet(set);
        System.out.println(integers);
    }

    @Test
    public void testSubstring() {
        String key = "user:auth:22";
        String userIdStr = key.substring(key.lastIndexOf(":") + 1);
        System.out.println("userIdStr = " + userIdStr);
    }


    private Set<Integer> convertToIntegerSet(Set<Object> objectSet) {
        Set<Integer> integerSet = new HashSet<>();

        for (Object obj : objectSet) {
            if (obj instanceof Integer) {
                integerSet.add((Integer) obj);
            } else if (obj instanceof String) {
                // If the elements are stored as Strings, convert them to Integer
                integerSet.add(Integer.parseInt((String) obj));
            }
            // Handle other types if needed
        }

        return integerSet;
    }
}
