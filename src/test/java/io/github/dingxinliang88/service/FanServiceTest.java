package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.pojo.po.Band;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static io.github.dingxinliang88.constants.CommonConstant.RELEASE;
import static org.junit.Assert.*;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FanServiceTest {

    @Resource
    private BandMapper bandMapper;

    @Test
    public void listMyLikedBandByPage() {
        LambdaQueryWrapper<Band> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.inSql(Band::getBandId, "SELECT band_id FROM band_like\n" +
                        "WHERE user_id = " + 22)
                .eq(Band::getIsRelease, RELEASE);
        Page<Band> bandPage = bandMapper.selectPage(new Page<>(1, 5), queryWrapper);
        System.out.println(bandPage);
    }
}