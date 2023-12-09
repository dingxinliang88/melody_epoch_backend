package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.service.SongService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 歌曲模块
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@RestController
@RequestMapping("/song")
public class SongController {

    @Resource
    private SongService songService;
}
