package io.github.dingxinliang88.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dingxinliang88.aspect.limiter.MelodyRateLimiter;
import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.band.AddBandReq;
import io.github.dingxinliang88.pojo.dto.band.EditBandReq;
import io.github.dingxinliang88.pojo.vo.band.BandDetailsVO;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;
import io.github.dingxinliang88.service.BandService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 乐队模块
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@RestController
@RequestMapping("/band")
public class BandController {

    @Resource
    private BandService bandService;

    @PostMapping("/add")
    public BaseResponse<Integer> addBand(@RequestBody @Validated AddBandReq req) {
        return RespUtil.success(bandService.addBand(req));
    }

    @PutMapping("/edit")
    public BaseResponse<Boolean> editBandInfo(@RequestBody @Validated EditBandReq req) {
        return RespUtil.success(bandService.editBandInfo(req));
    }

    /**
     * @see BandController#listBandBriefInfoByPage(Integer)
     * @deprecated
     */
    @Deprecated
    @GetMapping("/list")
    public BaseResponse<List<BandInfoVO>> listBandBriefInfo() {
        return RespUtil.success(bandService.listBandBriefInfo());
    }

    @MelodyRateLimiter
    @GetMapping("/list/page")
    public BaseResponse<Page<BandInfoVO>> listBandBriefInfoByPage(@RequestParam(value = "curr") @NotNull @Min(1) Integer current) {
        return RespUtil.success(bandService.listBandBriefInfoByPage(current));
    }

    @GetMapping("/list/all")
    public BaseResponse<BandDetailsVO> listBandInfoVO(@RequestParam(value = "bandId") @NotNull(message = "乐队ID不能为空") Integer bandId) {
        return RespUtil.success(bandService.listBandInfoVO(bandId));
    }

    @GetMapping("/curr")
    public BaseResponse<BandDetailsVO> listCurrBandInfoVO() {
        return RespUtil.success(bandService.listCurrBandInfoVO());
    }

    @GetMapping("/release/status")
    public BaseResponse<Integer> queryCurrBandReleaseStatus() {
        return RespUtil.success(bandService.queryCurrBandReleaseStatus());
    }

    @PostMapping("/release")
    public BaseResponse<Boolean> releaseBand() {
        return RespUtil.success(bandService.releaseBand());
    }

    @PostMapping("/unrelease")
    public BaseResponse<Boolean> unReleaseBand() {
        return RespUtil.success(bandService.unReleaseBand());
    }
}
