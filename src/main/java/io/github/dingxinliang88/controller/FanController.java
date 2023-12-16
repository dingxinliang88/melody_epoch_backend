package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.LikeReq;
import io.github.dingxinliang88.pojo.dto.fan.EditFanReq;
import io.github.dingxinliang88.pojo.dto.fan.ScoreAlbumReq;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;
import io.github.dingxinliang88.pojo.vo.fan.LikeAlbumStatusVO;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.service.FanService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @PostMapping("/like")
    public BaseResponse<Boolean> like(@RequestBody @Validated LikeReq req, HttpServletRequest request) {
        return RespUtil.success(fanService.like(req, request));
    }

    @PostMapping("/unlike")
    public BaseResponse<Boolean> unlike(@RequestBody @Validated LikeReq req, HttpServletRequest request) {
        return RespUtil.success(fanService.unlike(req, request));
    }

    @GetMapping("/like/album/status")
    public BaseResponse<LikeAlbumStatusVO> getLikeAlbumStatus(@RequestParam(value = "albumId") @NotNull Integer albumId,
                                                              HttpServletRequest request) {
        return RespUtil.success(fanService.getLikeAlbumStatus(albumId, request));
    }

    @PostMapping("/score")
    public BaseResponse<Boolean> scoreAlbum(@RequestBody @Validated ScoreAlbumReq req, HttpServletRequest request) {
        return RespUtil.success(fanService.scoreAlbum(req, request));
    }

    @GetMapping("/like/band")
    public BaseResponse<List<BandInfoVO>> listMyLikedBand(HttpServletRequest request) {
        return RespUtil.success(fanService.listMyLikedBand(request));
    }

    @GetMapping("/like/album")
    public BaseResponse<List<AlbumInfoVO>> listMyLikedAlbum(HttpServletRequest request) {
        return RespUtil.success(fanService.listMyLikedAlbum(request));
    }

    @GetMapping("/like/song")
    public BaseResponse<List<SongInfoVO>> listMyLikedSong(HttpServletRequest request) {
        return RespUtil.success(fanService.listMyLikedSong(request));
    }

}
