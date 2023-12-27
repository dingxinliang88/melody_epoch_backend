package io.github.dingxinliang88.manager.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.mapper.ConcertMapper;
import io.github.dingxinliang88.mapper.SongLikeMapper;
import io.github.dingxinliang88.pojo.dto.QueryReq;
import io.github.dingxinliang88.pojo.po.Concert;
import io.github.dingxinliang88.pojo.vo.SearchVO;
import io.github.dingxinliang88.pojo.vo.concert.ConcertInfoVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索演唱会信息策略
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Component
public class ConcertSearchStrategy implements SearchStrategy {

    @Resource
    private ConcertMapper concertMapper;

    @Resource
    private BandMapper bandMapper;

    @Resource
    private SongLikeMapper songLikeMapper;

    @Override
    public SearchVO doSearch(QueryReq req) {

        String searchText = req.getSearchText();
        Long current = req.getCurrent();
        Long pageSize = req.getPageSize();
        String sortOrder = req.getSortOrder();

        LambdaQueryWrapper<Concert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(Concert::getName, searchText); // like searchText%;
        queryWrapper.orderBy(true, CommonConstant.SORT_ORDER_ASC.equals(sortOrder), Concert::getName);
        Page<Concert> concertPage = concertMapper.selectPage(new Page<>(current, pageSize), queryWrapper);

        Page<ConcertInfoVO> songInfoVOPage = convertConcertInfoVOPage(concertPage);

        // 构造结果
        SearchVO searchVO = new SearchVO();
        searchVO.setConcertInfoVOPage(songInfoVOPage);
        return searchVO;
    }

    // --------------------------
    // private util function
    // --------------------------

    private Page<ConcertInfoVO> convertConcertInfoVOPage(Page<Concert> concertPage) {
        Page<ConcertInfoVO> concertInfoVOPage
                = new Page<>(concertPage.getCurrent(), concertPage.getSize(), concertPage.getTotal(), concertPage.searchCount());

        List<ConcertInfoVO> concertInfoVOList = concertPage.getRecords().stream().map(ConcertInfoVO::concertToVO).collect(Collectors.toList());
        concertInfoVOPage.setRecords(concertInfoVOList);
        return concertInfoVOPage;
    }
}
