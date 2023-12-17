package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.member.EditMemberReq;
import io.github.dingxinliang88.pojo.dto.member.EditPartReq;
import io.github.dingxinliang88.pojo.dto.member.JoinBandReq;
import io.github.dingxinliang88.pojo.vo.member.MemberInfoVO;
import io.github.dingxinliang88.service.MemberService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public BaseResponse<Boolean> joinBand(@RequestBody @Validated JoinBandReq req, HttpServletRequest request) {
        return RespUtil.success(memberService.joinBand(req, request));
    }

    @PostMapping("/leave")
    public BaseResponse<Boolean> leaveBand(@RequestBody @Validated  JoinBandReq req, HttpServletRequest request) {
        return RespUtil.success(memberService.leaveBand(req, request));
    }

    @PutMapping("/edit")
    public BaseResponse<Boolean> editInfo(@RequestBody @Validated EditMemberReq req, HttpServletRequest request) {
        return RespUtil.success(memberService.editInfo(req, request));
    }

    @PutMapping("/part")
    public BaseResponse<Boolean> editMemberPart(@RequestBody @Validated EditPartReq req, HttpServletRequest request) {
        return RespUtil.success(memberService.editMemberPart(req, request));
    }

    @GetMapping("/list")
    public BaseResponse<List<MemberInfoVO>> listMembers(HttpServletRequest request) {
        return RespUtil.success(memberService.listMembers(request));
    }

    @GetMapping("/curr_band")
    public BaseResponse<List<MemberInfoVO>> listMemberInCurrBand(HttpServletRequest request) {
        return RespUtil.success(memberService.listMemberInCurrBand(request));
    }

}
