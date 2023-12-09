package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.service.ConcertService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
}
