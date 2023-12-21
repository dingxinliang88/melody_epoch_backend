package io.github.dingxinliang88.controller;

import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.album.AddAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.EditAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.ReleaseAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.SongToAlbumReq;
import io.github.dingxinliang88.pojo.vo.album.AlbumDetailsVO;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import io.github.dingxinliang88.service.AlbumService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    public BaseResponse<Integer> addAlbum(@RequestBody @Validated AddAlbumReq req) {
        return RespUtil.success(albumService.addAlbum(req));
    }

    @PutMapping("/edit")
    public BaseResponse<Boolean> editInfo(@RequestBody @Validated EditAlbumReq req) {
        return RespUtil.success(albumService.editInfo(req));
    }

    // TODO 分页获取
    @GetMapping("/list")
    public BaseResponse<List<AlbumInfoVO>> listAlbumInfoVO() {
        return RespUtil.success(albumService.listAlbumInfoVO());
    }

    @GetMapping("/info")
    public BaseResponse<AlbumDetailsVO> getAlbumDetailsInfo(@RequestParam(value = "albumId") @NotNull Integer albumId) {
        return RespUtil.success(albumService.getAlbumDetailsInfo(albumId));
    }

    @GetMapping("/curr")
    public BaseResponse<List<AlbumInfoVO>> currBandAllAlbums() {
        return RespUtil.success(albumService.currBandAllAlbums());
    }

    @PutMapping("/songs")
    public BaseResponse<Boolean> addSongsToAlbum(@RequestBody @Validated SongToAlbumReq req) {
        return RespUtil.success(albumService.addSongsToAlbum(req));
    }

    @PostMapping("/release")
    public BaseResponse<Boolean> releaseAlbum(@RequestBody @Validated ReleaseAlbumReq req) {
        return RespUtil.success(albumService.releaseAlbum(req));
    }
}
