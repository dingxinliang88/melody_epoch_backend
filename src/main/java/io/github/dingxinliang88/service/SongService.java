package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.song.AddSongReq;
import io.github.dingxinliang88.pojo.dto.song.EditSongReq;
import io.github.dingxinliang88.pojo.po.Song;

import javax.servlet.http.HttpServletRequest;

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
     * 修改歌曲信息（专辑信息）
     *
     * @param req     修改歌曲请求
     * @param request http request
     * @return true - 修改成功
     */
    Boolean editInfo(EditSongReq req, HttpServletRequest request);
}
