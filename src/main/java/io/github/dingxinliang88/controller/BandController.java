package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.band.AddBandReq;
import io.github.dingxinliang88.pojo.dto.band.EditBandReq;
import io.github.dingxinliang88.service.BandService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<Integer> addBand(@RequestBody @Validated AddBandReq req, HttpServletRequest request) {
        return RespUtil.success(bandService.addBand(req, request));
    }

    @PutMapping("/edit")
    public BaseResponse<Boolean> editInfo(@RequestBody @Validated EditBandReq req, HttpServletRequest request) {
        return RespUtil.success(bandService.editInfo(req, request));
    }


}
