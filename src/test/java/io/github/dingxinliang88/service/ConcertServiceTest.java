package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dingxinliang88.pojo.vo.concert.ConcertInfoVO;
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
public class ConcertServiceTest {

    @Resource
    private ConcertService concertService;

    @Test
    public void listConcertInfoVOByPage() {
        Page<ConcertInfoVO> concertInfoVOPage = concertService.listConcertInfoVOByPage(2);
        System.out.println(concertInfoVOPage);
    }
}