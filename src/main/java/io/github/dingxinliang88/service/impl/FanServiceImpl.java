package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.aspect.auth.LoginFunc;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.mapper.FanMapper;
import io.github.dingxinliang88.pojo.dto.fan.EditInfoReq;
import io.github.dingxinliang88.pojo.po.Fan;
import io.github.dingxinliang88.pojo.vo.UserLoginVO;
import io.github.dingxinliang88.service.FanService;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    @Override
    @LoginFunc
    public Boolean editInfo(EditInfoReq req, HttpServletRequest request) {

        Integer fanId = req.getFanId();

        // 检查是否是本人
        UserLoginVO currUser = SysUtil.getCurrUser(request);
        ThrowUtil.throwIf(!currUser.getUserId().equals(fanId), StatusCode.NO_AUTH_ERROR,
                "无权修改其他乐迷信息！");

        // 查找相关的成员是否存在
        Fan fan = fanMapper.queryByFanId(fanId);
        ThrowUtil.throwIf(fan == null, StatusCode.NOT_FOUND_ERROR, "未查找到相关成员信息！");

        // 更新相关的信息
        return fanMapper.updateInfo(req);
    }
}
