package io.github.dingxinliang88.mapper;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * User Mapper Test
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {

    private final Logger logger = LoggerFactory.getLogger(UserMapperTest.class);

    @Resource
    UserMapper userMapper;

    @Test
    public void testInsert() {
        String salt = RandomUtil.randomString(16);
        String originPassword = "admin123";
        String encryptedPassword = DigestUtil.md5Hex(salt + originPassword, StandardCharsets.UTF_8);
        User user = new User();
        user.setType(UserRoleType.ADMIN.getType());
        user.setAccount("admin");
        user.setPassword(encryptedPassword);
        user.setSalt(salt);
        user.setPhone("17474117411");
        user.setEmail("codejuzi@qq.com");
        logger.info("user info: {}", JSONUtil.toJsonStr(user));
        userMapper.insert(user);
    }

}