package io.github.dingxinliang88.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.concert.AddConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.EditConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.JoinConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.ReleaseConcertReq;
import io.github.dingxinliang88.pojo.vo.concert.ConcertDetailsVO;
import io.github.dingxinliang88.pojo.vo.concert.ConcertInfoVO;
import io.github.dingxinliang88.pojo.vo.concert.ConcertJoinInfoVO;
import io.github.dingxinliang88.service.ConcertService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 演唱会模块
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@RestController
@RequestMapping("/concert")
public class ConcertController {

    @Resource
    private ConcertService concertService;

    @PostMapping("/add")
    public BaseResponse<Long> addConcert(@RequestBody @Validated AddConcertReq req) {
        return RespUtil.success(concertService.addConcert(req));
    }

    @PutMapping("/edit")
    public BaseResponse<Boolean> editConcertInfo(@RequestBody @Validated EditConcertReq req) {
        return RespUtil.success(concertService.editConcertInfo(req));
    }

    /**
     * @see ConcertController#listConcertInfoVOByPage(Integer)
     * @deprecated
     */
    @Deprecated
    @GetMapping("/list")
    public BaseResponse<List<ConcertInfoVO>> listConcertInfoVO() {
        return RespUtil.success(concertService.listConcertInfoVO());
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<ConcertInfoVO>> listConcertInfoVOByPage(@RequestParam(value = "curr") @NotNull @Min(1) Integer current) {
        return RespUtil.success(concertService.listConcertInfoVOByPage(current));
    }

    @PostMapping("/release")
    public BaseResponse<Boolean> releaseConcert(@RequestBody @Validated ReleaseConcertReq req) {
        return RespUtil.success(concertService.releaseConcert(req));
    }

    @PostMapping("/unrelease")
    public BaseResponse<Boolean> unReleaseConcert(@RequestBody @Validated ReleaseConcertReq req) {
        return RespUtil.success(concertService.unReleaseConcert(req));
    }

    /**
     * @see ConcertController#getCurrConcertInfoByPage(Integer, Integer)
     * @deprecated
     */
    @Deprecated
    @GetMapping("/curr")
    public BaseResponse<List<ConcertInfoVO>> getCurrConcertInfo() {
        return RespUtil.success(concertService.getCurrConcertInfo());
    }

    @GetMapping("/curr/page")
    public BaseResponse<Page<ConcertInfoVO>> getCurrConcertInfoByPage(@RequestParam(value = "curr") @NotNull @Min(1) Integer current,
                                                                      @RequestParam(value = "size") @NotNull @Min(5) @Max(50) Integer size) {
        return RespUtil.success(concertService.getCurrConcertInfoByPage(current, size));
    }

    @GetMapping("/band/page")
    public BaseResponse<Page<ConcertInfoVO>> getBandConcertInfoByPage(@RequestParam(value = "bandId") @NotNull(message = "乐队ID不能为空") Integer bandId,
                                                                      @RequestParam(value = "curr") @NotNull @Min(1) Integer current,
                                                                      @RequestParam(value = "size") @NotNull @Min(5) @Max(50) Integer size) {
        return RespUtil.success(concertService.getBandConcertInfoByPage(bandId, current, size));
    }

    @GetMapping("/all")
    public BaseResponse<ConcertDetailsVO> getCurrConcertDetails(@RequestParam(value = "concertId") @NotNull Long concertId) {
        return RespUtil.success(concertService.getCurrConcertDetails(concertId));
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinConcert(@RequestBody @Validated JoinConcertReq req) {
        return RespUtil.success(concertService.joinConcert(req));
    }

    @PostMapping("/leave")
    public BaseResponse<Boolean> leaveConcert(@RequestBody @Validated JoinConcertReq req) {
        return RespUtil.success(concertService.leaveConcert(req));
    }

    @GetMapping("/join/status")
    public BaseResponse<ConcertJoinInfoVO> getCurrConcertJoinInfo(@RequestParam(value = "concertId") @NotNull Long concertId) {
        return RespUtil.success(concertService.getCurrConcertJoinInfo(concertId));
    }
}
