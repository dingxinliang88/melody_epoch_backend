package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.song.AddSongReq;
import io.github.dingxinliang88.pojo.dto.song.ReleaseSongReq;
import io.github.dingxinliang88.pojo.po.Song;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.pojo.vo.song.SongItemVO;
import io.github.dingxinliang88.pojo.vo.song.SongToAlbumVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Song Service
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public interface SongService extends IService<Song> {

    /**
     * 添加歌曲信息
     *
     * @param req     添加歌曲请求
     * @param request http request
     * @return 歌曲ID
     */
    Integer addSong(AddSongReq req, HttpServletRequest request);

    /**
     * 获取歌曲元素VO
     *
     * @param request http request
     * @return song items vo list
     */
    List<SongItemVO> listSongItems(HttpServletRequest request);

    /**
     * 获取已经发布的歌曲信息
     *
     * @param request http request
     * @return song info vo list
     */
    List<SongInfoVO> listSongInfoVO(HttpServletRequest request);

    /**
     * 查询已经录入当前专辑的歌曲信息和未录入专辑的歌曲信息
     *
     * @param albumId album id
     * @param request http request
     * @return song to album vo
     */
    SongToAlbumVO listSongToAlbum(Integer albumId, HttpServletRequest request);

    /**
     * 查询当前乐队的歌曲信息
     *
     * @param request http request
     * @return song info
     */
    List<Song> currBandSongs(HttpServletRequest request);

    /**
     * 发布歌曲信息
     *
     * @param req     发布歌曲信息请求
     * @param request http request
     * @return true - 发布成功
     */
    Boolean releaseSong(ReleaseSongReq req, HttpServletRequest request);
}
