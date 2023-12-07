package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.service.BandService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 乐队模块
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@RestController
@RequestMapping("/band")
public class BandController {

    @Resource
    private BandService bandService;
}
