package io.github.dingxinliang88.mapper;

import io.github.dingxinliang88.pojo.dto.fan.EditFanReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static io.github.dingxinliang88.constants.UserConstant.MALE;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FanMapperTest {

    @Resource
    private FanMapper fanMapper;

    @Test
    public void updateInfo() {

        EditFanReq req = new EditFanReq();
        req.setFanId(2);
        req.setName("张三");
        req.setGender(MALE);
        req.setAge(20);
        req.setCareer("IT");
        req.setEducation("本科");

        fanMapper.updateInfoByFanId(req);
    }

}