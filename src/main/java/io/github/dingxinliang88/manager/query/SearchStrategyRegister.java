package io.github.dingxinliang88.manager.query;

import io.github.dingxinliang88.pojo.enums.SearchType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 搜索策略注册器
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Component
public class SearchStrategyRegister {

    @Resource
    private BandSearchStrategy bandSearchStrategy;

    @Resource
    private AlbumSearchStrategy albumSearchStrategy;

    @Resource
    private SongSearchStrategy songSearchStrategy;

    @Resource
    private ConcertSearchStrategy concertSearchStrategy;

    private static final Map<Integer, SearchStrategy> SEARCH_STRATEGY_MAP = new ConcurrentHashMap<>(16);

    @PostConstruct
    public void init() {
        SEARCH_STRATEGY_MAP.put(SearchType.BAND.getType(), bandSearchStrategy);
        SEARCH_STRATEGY_MAP.put(SearchType.ALBUM.getType(), albumSearchStrategy);
        SEARCH_STRATEGY_MAP.put(SearchType.SONG.getType(), songSearchStrategy);
        SEARCH_STRATEGY_MAP.put(SearchType.CONCERT.getType(), concertSearchStrategy);
    }


    /**
     * 根据获取搜索策略
     *
     * @param type search type
     * @return search strategy
     */
    public SearchStrategy getSearchStrategy(SearchType type) {
        return SEARCH_STRATEGY_MAP.get(type.getType());
    }

}
