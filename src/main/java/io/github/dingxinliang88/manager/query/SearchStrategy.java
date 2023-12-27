package io.github.dingxinliang88.manager.query;

import io.github.dingxinliang88.pojo.dto.QueryReq;
import io.github.dingxinliang88.pojo.vo.SearchVO;

/**
 * 查询接口
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public interface SearchStrategy {

    /**
     * 执行搜索
     *
     * @param req 搜索请求
     * @return search vo
     */
    SearchVO doSearch(QueryReq req);

}
