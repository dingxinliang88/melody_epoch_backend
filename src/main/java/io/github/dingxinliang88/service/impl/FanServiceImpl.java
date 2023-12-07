package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.mapper.FanMapper;
import io.github.dingxinliang88.pojo.po.Fan;
import io.github.dingxinliang88.service.FanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Fan Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class FanServiceImpl extends ServiceImpl<FanMapper, Fan>
        implements FanService {

    @Resource
    private FanMapper fanMapper;

}
