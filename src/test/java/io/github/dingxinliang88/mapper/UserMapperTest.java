package io.github.dingxinliang88.mapper;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.User;
import io.github.dingxinliang88.utils.SysUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * User Mapper Test
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
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
        String encryptedPassword = SysUtil.encryptedPwd(salt, originPassword);
        User user = new User();
        user.setType(UserRoleType.ADMIN.getType());
        user.setAccount("admin");
        user.setNickname("Admin");
        user.setPassword(encryptedPassword);
        user.setSalt(salt);
        user.setEmail("codejuzi@qq.com");
        logger.info("user info: {}", JSONUtil.toJsonStr(user));
        userMapper.insert(user);
    }

    @Test
    public void queryByEmail() {
        String email = "codejuzi@qq.com";
        User user = userMapper.queryByEmail(email);
        System.out.println(JSONUtil.toJsonStr(user));
    }
}