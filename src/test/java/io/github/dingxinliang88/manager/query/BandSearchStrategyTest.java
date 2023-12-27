package io.github.dingxinliang88.manager.query;

import io.github.dingxinliang88.pojo.dto.QueryReq;
import io.github.dingxinliang88.pojo.vo.SearchVO;
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
public class BandSearchStrategyTest {

    @Resource
    private BandSearchStrategy bandSearchStrategy;

    @Test
    public void doSearch() {
        QueryReq req = new QueryReq();
        req.setSearchText("band"); // LIKE '%band%', LIKE 'band%'
        SearchVO searchVO = bandSearchStrategy.doSearch(req);
        System.out.println(searchVO);
    }
}