package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.band.AddBandReq;
import io.github.dingxinliang88.service.BandService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 乐队模块
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@RestController
@RequestMapping("/band")
public class BandController {

    @Resource
    private BandService bandService;

    @PostMapping("/add")
    public BaseResponse<Integer> addBand(@RequestBody AddBandReq req, HttpServletRequest request) {
        return RespUtil.success(bandService.addBand(req, request));
    }



}
