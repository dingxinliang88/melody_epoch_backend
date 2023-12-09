package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.mapper.ConcertMapper;
import io.github.dingxinliang88.pojo.po.Concert;
import io.github.dingxinliang88.service.ConcertService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Comment Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class ConcertServiceImpl extends ServiceImpl<ConcertMapper, Concert>
        implements ConcertService {

    @Resource
    private ConcertMapper concertMapper;

}
