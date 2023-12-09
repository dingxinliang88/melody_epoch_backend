package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.member.EditInfoReq;
import io.github.dingxinliang88.pojo.dto.member.JoinBandReq;
import io.github.dingxinliang88.pojo.dto.member.LeaveBandReq;
import io.github.dingxinliang88.service.MemberService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 乐队成员模块
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @PostMapping("/join")
    public BaseResponse<Boolean> joinBand(@RequestBody JoinBandReq req, HttpServletRequest request) {
        return RespUtil.success(memberService.joinBand(req, request));
    }

    @PostMapping("/leave")
    public BaseResponse<Boolean> leaveBand(@RequestBody LeaveBandReq req, HttpServletRequest request) {
        return RespUtil.success(memberService.leaveBand(req, request));
    }

    @PutMapping("/edit")
    public BaseResponse<Boolean> editInfo(@RequestBody EditInfoReq req, HttpServletRequest request) {
        return RespUtil.success(memberService.editInfo(req, request));
    }
}
