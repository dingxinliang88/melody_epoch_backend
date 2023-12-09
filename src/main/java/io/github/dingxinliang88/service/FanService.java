package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.fan.EditInfoReq;
import io.github.dingxinliang88.pojo.po.Fan;

import javax.servlet.http.HttpServletRequest;

/**
 * Fan Service
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public interface FanService extends IService<Fan> {

    /**
     * 乐迷修改自己的信息
     *
     * @param req     修改信息请求
     * @param request http request
     * @return true - 修改成功
     */
    Boolean editInfo(EditInfoReq req, HttpServletRequest request);
}
