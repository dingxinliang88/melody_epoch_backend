package io.github.dingxinliang88.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dingxinliang88.biz.BaseResponse;
import io.github.dingxinliang88.pojo.dto.album.AddAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.EditAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.ReleaseAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.SongToAlbumReq;
import io.github.dingxinliang88.pojo.vo.album.AlbumDetailsVO;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import io.github.dingxinliang88.pojo.vo.album.TopAlbumVO;
import io.github.dingxinliang88.service.AlbumService;
import io.github.dingxinliang88.utils.RespUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 专辑模块
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
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
    public BaseResponse<Boolean> editAlbumInfo(@RequestBody @Validated EditAlbumReq req) {
        return RespUtil.success(albumService.editAlbumInfo(req));
    }

    /**
     * @see AlbumController#listAlbumInfoByPage(Integer)
     * @deprecated
     */
    @Deprecated
    @GetMapping("/list")
    public BaseResponse<List<AlbumInfoVO>> listAlbumInfo() {
        return RespUtil.success(albumService.listAlbumInfo());
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<AlbumInfoVO>> listAlbumInfoByPage(@RequestParam(value = "curr") @NotNull @Min(1) Integer current) {
        return RespUtil.success(albumService.listAlbumInfoByPage(current));
    }

    @GetMapping("/info")
    public BaseResponse<AlbumDetailsVO> getAlbumDetailsInfo(@RequestParam(value = "albumId") @NotNull Integer albumId) {
        return RespUtil.success(albumService.getAlbumDetailsInfo(albumId));
    }

    @GetMapping("/curr")
    public BaseResponse<List<AlbumInfoVO>> getCurrBandAllAlbums() {
        return RespUtil.success(albumService.getCurrBandAllAlbums());
    }

    @GetMapping("/curr/page")
    public BaseResponse<Page<AlbumInfoVO>> getCurrBandAllAlbumsByPage(@RequestParam(value = "curr") @NotNull @Min(1) Integer current,
                                                                      @RequestParam(value = "size") @NotNull @Min(5) @Max(50) Integer size) {
        return RespUtil.success(albumService.getCurrBandAllAlbumsByPage(current, size));
    }

    @GetMapping("/band/page")
    public BaseResponse<Page<AlbumInfoVO>> getBandAlbumsByPage(@RequestParam(value = "bandId") @NotNull(message = "乐队ID不能为空") Integer bandId,
                                                               @RequestParam(value = "curr") @NotNull @Min(1) Integer current,
                                                               @RequestParam(value = "size") @NotNull @Min(5) @Max(50) Integer size) {
        return RespUtil.success(albumService.getBandAlbumsByPage(bandId, current, size));
    }

    @PutMapping("/songs")
    public BaseResponse<Boolean> addSongsToAlbum(@RequestBody @Validated SongToAlbumReq req) {
        return RespUtil.success(albumService.addSongsToAlbum(req));
    }

    @PostMapping("/release")
    public BaseResponse<Boolean> releaseAlbum(@RequestBody @Validated ReleaseAlbumReq req) {
        return RespUtil.success(albumService.releaseAlbum(req));
    }

    @GetMapping("/top")
    public BaseResponse<List<TopAlbumVO>> getTopAlbums() {
        return RespUtil.success(albumService.topAlbums());
    }
}
