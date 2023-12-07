package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.service.BandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Band Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class BandServiceImpl extends ServiceImpl<BandMapper, Band>
        implements BandService {

    @Resource
    private BandMapper bandMapper;

}
