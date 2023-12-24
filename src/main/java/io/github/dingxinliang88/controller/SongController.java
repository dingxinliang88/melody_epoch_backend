package io.github.dingxinliang88.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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

    /**
     * @see SongController#listSongInfoVOByPage(Integer)
     * @deprecated
     */
    @Deprecated
    @GetMapping("/list")
    public BaseResponse<List<SongInfoVO>> listSongInfoVO() {
        return RespUtil.success(songService.listSongInfoVO());
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<SongInfoVO>> listSongInfoVOByPage(@RequestParam(value = "curr") @NotNull @Min(1) Integer current) {
        return RespUtil.success(songService.listSongInfoVOByPage(current));
    }

    @GetMapping("/album/to")
    public BaseResponse<SongToAlbumVO> listSongToAlbum(@RequestParam("albumId") Integer albumId) {
        return RespUtil.success(songService.listSongToAlbum(albumId));
    }

    /**
     * @see SongController#currBandSongsByPage(Integer, Integer)
     * @deprecated
     */
    @Deprecated
    @GetMapping("/curr")
    public BaseResponse<List<Song>> currBandSongs() {
        return RespUtil.success(songService.currBandSongs());
    }

    @GetMapping("/curr/page")
    public BaseResponse<Page<Song>> currBandSongsByPage(@RequestParam(value = "curr") @NotNull @Min(1) Integer current,
                                                        @RequestParam(value = "size") @NotNull @Min(5) Integer size) {
        return RespUtil.success(songService.currBandSongsByPage(current, size));
    }

    @GetMapping("/band/page")
    public BaseResponse<Page<Song>> getBandSongsByPage(@RequestParam(value = "bandId") @NotNull(message = "乐队ID不能为空") Integer bandId,
                                                       @RequestParam(value = "curr") @NotNull @Min(1) Integer current,
                                                       @RequestParam(value = "size") @NotNull @Min(5) Integer size) {
        return RespUtil.success(songService.getBandSongsByPage(bandId, current, size));
    }

    @GetMapping("/album/page")
    public BaseResponse<Page<Song>> getAlbumSongsByPage(@RequestParam(value = "albumId") @NotNull(message = "专辑不能为空") Integer albumId,
                                                        @RequestParam(value = "curr") @NotNull @Min(1) Integer current,
                                                        @RequestParam(value = "size") @NotNull @Min(5) Integer size) {
        return RespUtil.success(songService.getAlbumSongsByPage(albumId, current, size));
    }

    @PostMapping("/release")
    public BaseResponse<Boolean> releaseSong(@RequestBody @Validated ReleaseSongReq req) {
        return RespUtil.success(songService.releaseSong(req));
    }
}
