package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.song.AddSongReq;
import io.github.dingxinliang88.pojo.dto.song.EditSongReq;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.pojo.vo.song.SongItemVO;
import io.github.dingxinliang88.service.SongService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @PostMapping("/add")
    public BaseResponse<Integer> addSong(@RequestBody @Validated AddSongReq req, HttpServletRequest request) {
        return RespUtil.success(songService.addSong(req, request));
    }

    @PutMapping("/edit")
    public BaseResponse<Boolean> editInfo(@RequestBody @Validated EditSongReq req, HttpServletRequest request) {
        return RespUtil.success(songService.editInfo(req, request));
    }

    @GetMapping("/items")
    public BaseResponse<List<SongItemVO>> listSongItems(HttpServletRequest request) {
        return RespUtil.success(songService.listSongItems(request));
    }

    @GetMapping("/list")
    public BaseResponse<List<SongInfoVO>> listSongInfoVO(HttpServletRequest request) {
        return RespUtil.success(songService.listSongInfoVO(request));
    }
}
