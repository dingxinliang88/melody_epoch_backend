package io.github.dingxinliang88.mapper;

import cn.hutool.json.JSONUtil;
import io.github.dingxinliang88.pojo.dto.band.EditBandReq;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BandMapperTest {

    @Resource
    private BandMapper bandMapper;

    @Test
    public void editInfo() {
        EditBandReq req = new EditBandReq();
        req.setBandId(2);
        req.setProfile("Test Update Band Info");
        bandMapper.editInfo(req);
    }

    @Test
    public void queryBandInfoVOByBandId() {
        Integer bandId = 1;
        BandInfoVO bandInfoVO = bandMapper.queryBandInfoVOByBandId(bandId);
        System.out.println(JSONUtil.toJsonStr(bandInfoVO));
    }
}