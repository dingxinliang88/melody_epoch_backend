package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.mapper.AlbumMapper;
import io.github.dingxinliang88.pojo.po.Album;
import io.github.dingxinliang88.service.AlbumService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Album Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album>
        implements AlbumService {

    @Resource
    private AlbumMapper albumMapper;

}
