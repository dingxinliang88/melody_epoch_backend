package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.service.AlbumService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 专辑模块
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@RestController
@RequestMapping("/album")
public class AlbumController {

    @Resource
    private AlbumService albumService;
}
