package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.concert.AddConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.EditConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.ReleaseConcertReq;
import io.github.dingxinliang88.pojo.vo.concert.ConcertDetailsVO;
import io.github.dingxinliang88.pojo.vo.concert.ConcertInfoVO;
import io.github.dingxinliang88.service.ConcertService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 演唱会模块
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
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
    public BaseResponse<Boolean> editInfo(@RequestBody @Validated EditConcertReq req) {
        return RespUtil.success(concertService.editInfo(req));
    }

    @GetMapping("/list")
    public BaseResponse<List<ConcertInfoVO>> listConcertInfoVO() {
        return RespUtil.success(concertService.listConcertInfoVO());
    }

    @PostMapping("/release")
    public BaseResponse<Boolean> releaseConcert(@RequestBody @Validated ReleaseConcertReq req) {
        return RespUtil.success(concertService.releaseConcert(req));
    }

    @PostMapping("/unrelease")
    public BaseResponse<Boolean> unReleaseConcert(@RequestBody @Validated ReleaseConcertReq req) {
        return RespUtil.success(concertService.unReleaseConcert(req));
    }

    @GetMapping("/curr")
    public BaseResponse<List<ConcertInfoVO>> getCurrConcertInfo() {
        return RespUtil.success(concertService.getCurrConcertInfo());
    }

    @GetMapping("/all")
    public BaseResponse<ConcertDetailsVO> getCurrConcertDetails(@RequestParam(value = "concertId") @NotNull Long concertId) {
        return RespUtil.success(concertService.getCurrConcertDetails(concertId));
    }

}
