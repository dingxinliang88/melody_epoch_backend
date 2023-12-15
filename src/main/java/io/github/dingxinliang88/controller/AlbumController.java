package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.album.AddAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.EditAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.SongToAlbumReq;
import io.github.dingxinliang88.pojo.po.Album;
import io.github.dingxinliang88.pojo.vo.album.AlbumDetailsVO;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import io.github.dingxinliang88.service.AlbumService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @PostMapping("/add")
    public BaseResponse<Integer> addAlbum(@RequestBody @Validated AddAlbumReq req, HttpServletRequest request) {
        return RespUtil.success(albumService.addAlbum(req, request));
    }

    @PutMapping("/edit")
    public BaseResponse<Boolean> editInfo(@RequestBody @Validated EditAlbumReq req, HttpServletRequest request) {
        return RespUtil.success(albumService.editInfo(req, request));
    }

    @GetMapping("/list")
    public BaseResponse<List<AlbumInfoVO>> listAlbumInfoVO(HttpServletRequest request) {
        return RespUtil.success(albumService.listAlbumInfoVO(request));
    }

    @GetMapping("/info")
    public BaseResponse<AlbumDetailsVO> getAlbumDetailsInfo(@RequestParam(value = "albumId") @NotNull Integer albumId,
                                                            HttpServletRequest request) {
        return RespUtil.success(albumService.getAlbumDetailsInfo(albumId, request));
    }

    @GetMapping("/curr/all")
    public BaseResponse<List<Album>> currBandAllAlbums(HttpServletRequest request) {
        return RespUtil.success(albumService.currBandAllAlbums(request));
    }

    @PutMapping("/songs")
    public BaseResponse<Boolean> addSongsToAlbum(@RequestBody @Validated SongToAlbumReq req, HttpServletRequest request) {
        return RespUtil.success(albumService.addSongsToAlbum(req, request));
    }

    @PostMapping("/score")
    public BaseResponse<Boolean> scoreAlbum(@RequestBody @Validated ScoreAlbumReq req, HttpServletRequest request) {

    }
}
