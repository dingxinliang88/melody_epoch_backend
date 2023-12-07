package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.service.FanService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
}
