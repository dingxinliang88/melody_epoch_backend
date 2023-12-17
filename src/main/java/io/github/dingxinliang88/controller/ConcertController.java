package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.concert.AddConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.EditConcertReq;
import io.github.dingxinliang88.pojo.vo.concert.ConcertDetailsVO;
import io.github.dingxinliang88.pojo.vo.concert.ConcertInfoVO;
import io.github.dingxinliang88.pojo.dto.concert.JoinConcertReq;
import io.github.dingxinliang88.service.ConcertService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public BaseResponse<Long> addConcert(@RequestBody @Validated AddConcertReq req, HttpServletRequest request) {
        return RespUtil.success(concertService.addConcert(req, request));
    }

    @PutMapping("/edit")
    public BaseResponse<Boolean> editInfo(@RequestBody @Validated EditConcertReq req, HttpServletRequest request) {
        return RespUtil.success(concertService.editInfo(req, request));
    }

    @GetMapping("/list")
    public BaseResponse<List<ConcertInfoVO>> listConcertInfoVO(HttpServletRequest request) {
        return RespUtil.success(concertService.listConcertInfoVO(request));
    }

    @GetMapping("/all")
    public BaseResponse<ConcertDetailsVO> listCurrConcertInfoVO(@RequestParam(value = "concertId") @NotNull Long concertId,
                                                                HttpServletRequest request) {
        return RespUtil.success(concertService.listCurrConcertInfoVO(concertId, request));
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinConcert(@RequestBody @Validated JoinConcertReq req, HttpServletRequest request) {
        return RespUtil.success(concertService.joinConcert(req, request));
    }
    @PostMapping("/leave")
    public BaseResponse<Boolean> leaveConcert(@RequestBody @Validated JoinConcertReq req, HttpServletRequest request) {
        return RespUtil.success(concertService.leaveConcert(req, request));
    }
}
