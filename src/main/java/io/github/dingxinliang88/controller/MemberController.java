package io.github.dingxinliang88.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.member.EditPartReq;
import io.github.dingxinliang88.pojo.dto.member.JoinBandReq;
import io.github.dingxinliang88.pojo.vo.member.MemberInfoVO;
import io.github.dingxinliang88.service.MemberService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
    public BaseResponse<Boolean> joinBand(@RequestBody @Validated JoinBandReq req) {
        return RespUtil.success(memberService.joinBand(req));
    }

    @PostMapping("/leave")
    public BaseResponse<Boolean> leaveBand(@RequestBody @Validated JoinBandReq req) {
        return RespUtil.success(memberService.leaveBand(req));
    }

    @PutMapping("/part")
    public BaseResponse<Boolean> editMemberPart(@RequestBody @Validated EditPartReq req) {
        return RespUtil.success(memberService.editMemberPart(req));
    }

    @GetMapping("/list")
    public BaseResponse<List<MemberInfoVO>> listMemberInfoVO() {
        return RespUtil.success(memberService.listMemberInfoVO());
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<MemberInfoVO>> listMemberInfoVOByPage(@RequestParam(value = "curr") @NotNull @Min(1) Integer current) {
        return RespUtil.success(memberService.listMemberInfoVOByPage(current));
    }

    @GetMapping("/curr")
    public BaseResponse<List<MemberInfoVO>> listMemberInCurrBand() {
        return RespUtil.success(memberService.listMemberInCurrBand());
    }

    @GetMapping("/curr/page")
    public BaseResponse<Page<MemberInfoVO>> listMemberInCurrBandByPage(@RequestParam(value = "curr") @NotNull @Min(1) Integer current,
                                                                       @RequestParam(value = "size") @NotNull @Min(5) Integer size) {
        return RespUtil.success(memberService.listMemberInCurrBandByPage(current, size));
    }

}
