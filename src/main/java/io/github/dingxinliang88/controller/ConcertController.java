package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.concert.AddConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.EditConcertReq;
import io.github.dingxinliang88.service.ConcertService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

}
