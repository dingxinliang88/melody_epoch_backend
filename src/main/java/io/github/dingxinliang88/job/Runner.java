package io.github.dingxinliang88.job;

import io.github.dingxinliang88.constants.AlbumConstant;
import io.github.dingxinliang88.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Component
public class Runner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(Runner.class);

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void run(String... args) throws Exception {
        logger.info(">>>>>>>>>>>>> init epoch");
        redisUtil.setExpiredHours(AlbumConstant.TOP_ALBUM_CACHE_EPOCH_KEY, AlbumConstant.INIT_EPOCH,
                AlbumConstant.TOP_ALBUMS_EXPIRE_TIME);
    }
}
