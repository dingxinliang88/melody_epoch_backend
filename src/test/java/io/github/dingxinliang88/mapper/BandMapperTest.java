package io.github.dingxinliang88.mapper;

import cn.hutool.json.JSONUtil;
import io.github.dingxinliang88.pojo.dto.band.EditBandReq;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.vo.band.BandDetailsVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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
        BandDetailsVO bandDetailsVO = bandMapper.queryBandInfoVOByBandId(bandId, true);
        System.out.println(JSONUtil.toJsonStr(bandDetailsVO));
    }

    @Test
    public void queryByBandId() {
        Integer bandId = 1;
        Band band = bandMapper.queryByBandId(bandId, true);
        System.out.println(JSONUtil.toJsonStr(band));
    }

    @Test
    public void queryByLeaderId() {
        Integer leaderId = 16;
        Band band = bandMapper.queryByLeaderId(leaderId, true);
        System.out.println(JSONUtil.toJsonStr(band));
    }
}