package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.LikeReq;
import io.github.dingxinliang88.pojo.dto.fan.EditFanReq;
import io.github.dingxinliang88.pojo.dto.fan.ScoreAlbumReq;
import io.github.dingxinliang88.pojo.po.Fan;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;
import io.github.dingxinliang88.pojo.vo.fan.LikeAlbumStatusVO;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Fan Service
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public interface FanService extends IService<Fan> {

    /**
     * 乐迷修改自己的信息
     *
     * @param req     修改信息请求
     * @param request http request
     * @return true - 修改成功
     */
    Boolean editInfo(EditFanReq req, HttpServletRequest request);

    /**
     * 喜欢收藏
     *
     * @param req     喜欢收藏请求
     * @param request http request
     * @return true - 喜欢成功
     */
    Boolean like(LikeReq req, HttpServletRequest request);

    /**
     * 给专辑打分请求
     *
     * @param req     打分请求
     * @param request http request
     * @return true - 打分成功
     */
    Boolean scoreAlbum(ScoreAlbumReq req, HttpServletRequest request);

    /**
     * 获取喜欢专辑的状态
     *
     * @param albumId 专辑ID
     * @param request http request
     * @return 喜欢专辑状态
     */
    LikeAlbumStatusVO getLikeAlbumStatus(Integer albumId, HttpServletRequest request);

    /**
     * 撤销喜欢收藏
     *
     * @param req     撤销喜欢收藏请求
     * @param request http request
     * @return true - 撤销成功
     */
    Boolean unlike(LikeReq req, HttpServletRequest request);

    /**
     * 查询我喜欢的乐队
     *
     * @param request http request
     * @return like item list
     */
    List<BandInfoVO> listMyLikedBand(HttpServletRequest request);

    /**
     * 查询我喜欢的专辑
     *
     * @param request http request
     * @return like item list
     */
    List<AlbumInfoVO> listMyLikedAlbum(HttpServletRequest request);

    /**
     * 查询我喜欢的歌曲
     *
     * @param request http request
     * @return like item list
     */
    List<SongInfoVO> listMyLikedSong(HttpServletRequest request);
}
