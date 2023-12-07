package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.mapper.MemberMapper;
import io.github.dingxinliang88.pojo.po.Member;
import io.github.dingxinliang88.service.MemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Band Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member>
        implements MemberService {

    @Resource
    private MemberMapper memberMapper;

}
