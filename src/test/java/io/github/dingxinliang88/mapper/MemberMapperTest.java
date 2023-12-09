package io.github.dingxinliang88.mapper;

import cn.hutool.json.JSONUtil;
import io.github.dingxinliang88.pojo.dto.member.EditInfoReq;
import io.github.dingxinliang88.pojo.po.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static io.github.dingxinliang88.constants.UserConstant.MALE;


/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MemberMapperTest {


    private final Logger logger = LoggerFactory.getLogger(MemberMapperTest.class);

    @Resource
    MemberMapper memberMapper;

    @Test
    public void querySecondaryMember() {
        Integer bandId = 1;
        Member member = memberMapper.querySecondaryMember(bandId);
        logger.info("member info: {}", JSONUtil.toJsonStr(member));
    }

    @Test
    public void updateInfo() {
        EditInfoReq req = new EditInfoReq();
        req.setMemberId(8);
        req.setName("李四");
        req.setGender(MALE);
        req.setAge(18);
        memberMapper.updateInfo(req);
    }
}