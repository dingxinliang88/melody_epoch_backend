package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.song.AddSongReq;
import io.github.dingxinliang88.pojo.dto.song.ReleaseSongReq;
import io.github.dingxinliang88.pojo.po.Song;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.pojo.vo.song.SongItemVO;
import io.github.dingxinliang88.pojo.vo.song.SongToAlbumVO;
import io.github.dingxinliang88.service.SongService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    public BaseResponse<Integer> addSong(@RequestBody @Validated AddSongReq req) {
        return RespUtil.success(songService.addSong(req));
    }

    @GetMapping("/items")
    public BaseResponse<List<SongItemVO>> listSongItems() {
        return RespUtil.success(songService.listSongItems());
    }

    @GetMapping("/list")
    public BaseResponse<List<SongInfoVO>> listSongInfoVO() {
        return RespUtil.success(songService.listSongInfoVO());
    }

    @GetMapping("/album")
    public BaseResponse<SongToAlbumVO> listSongToAlbum(@RequestParam("albumId") Integer albumId) {
        return RespUtil.success(songService.listSongToAlbum(albumId));
    }

    @GetMapping("/curr")
    public BaseResponse<List<Song>> currBandSongs() {
        return RespUtil.success(songService.currBandSongs());
    }

    @PostMapping("/release")
    public BaseResponse<Boolean> releaseSong(@RequestBody @Validated ReleaseSongReq req) {
        return RespUtil.success(songService.releaseSong(req));
    }
}
