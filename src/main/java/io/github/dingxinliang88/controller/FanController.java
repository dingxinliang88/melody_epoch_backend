package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.fan.EditFanReq;
import io.github.dingxinliang88.service.FanService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 歌迷模块
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@RestController
@RequestMapping("/fan")
public class FanController {

    @Resource
    private FanService fanService;


    @PutMapping("/edit")
    public BaseResponse<Boolean> editInfo(@RequestBody EditFanReq req, HttpServletRequest request) {
        return RespUtil.success(fanService.editInfo(req, request));
    }
}
