package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.mapper.SongMapper;
import io.github.dingxinliang88.pojo.po.Song;
import io.github.dingxinliang88.service.SongService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Song Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class SongServiceImpl extends ServiceImpl<SongMapper, Song>
        implements SongService {

    @Resource
    private SongMapper songMapper;

}
