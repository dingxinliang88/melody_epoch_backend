package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.band.AddBandReq;
import io.github.dingxinliang88.pojo.dto.member.JoinBandReq;
import io.github.dingxinliang88.pojo.po.Band;

import javax.servlet.http.HttpServletRequest;

/**
 * Band Service
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public interface BandService extends IService<Band> {

    /**
     * 创建乐队
     *
     * @param req     创建乐队请求
     * @param request http request
     * @return band id
     */
    Integer addBand(AddBandReq req, HttpServletRequest request);


}
