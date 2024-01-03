package io.github.dingxinliang88.manager.query;

import io.github.dingxinliang88.pojo.dto.QueryReq;
import io.github.dingxinliang88.pojo.enums.SearchType;
import io.github.dingxinliang88.pojo.vo.SearchVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 搜索门面
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Component
public class SearchFacade {

    @Resource
    private SearchStrategyRegister searchStrategyRegister;

    /**
     * 执行搜索
     *
     * @param req 搜索请求
     * @return search vo
     */
    public SearchVO doSearch(QueryReq req) {
        Integer searchType = req.getSearchType();
        SearchType type = SearchType.getEnumByType(searchType);
        SearchStrategy searchStrategy = searchStrategyRegister.getSearchStrategy(type);
        return searchStrategy.doSearch(req);
    }

}
