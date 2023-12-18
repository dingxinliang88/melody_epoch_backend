package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.band.AddBandReq;
import io.github.dingxinliang88.pojo.dto.band.EditBandReq;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;
import io.github.dingxinliang88.pojo.vo.band.BandDetailsVO;
import io.github.dingxinliang88.service.BandService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @GetMapping("/list")
    public BaseResponse<List<BandInfoVO>> listBandBriefInfo(HttpServletRequest request) {
        return RespUtil.success(bandService.listBandBriefInfo(request));
    }

    @GetMapping("/list/all")
    public BaseResponse<BandDetailsVO> listBandInfoVO(@RequestParam(value = "bandId") @NotNull(message = "乐队ID不能为空") Integer bandId,
                                                      HttpServletRequest request) {
        return RespUtil.success(bandService.listBandInfoVO(bandId, request));
    }

    @GetMapping("/curr")
    public BaseResponse<BandDetailsVO> listCurrBandInfoVO(HttpServletRequest request) {
        return RespUtil.success(bandService.listCurrBandInfoVO(request));
    }

    @GetMapping("/release/status")
    public BaseResponse<Integer> queryCurrBandReleaseStatus(HttpServletRequest request) {
        return RespUtil.success(bandService.queryCurrBandReleaseStatus(request));
    }


    @PostMapping("/release")
    public BaseResponse<Boolean> releaseBand(HttpServletRequest request) {
        return RespUtil.success(bandService.releaseBand(request));
    }

    @PostMapping("/unrelease")
    public BaseResponse<Boolean> unReleaseBand(HttpServletRequest request) {
        return RespUtil.success(bandService.unReleaseBand(request));
    }
}
