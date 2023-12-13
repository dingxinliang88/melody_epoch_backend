package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.dto.song.EditSongReq;
import io.github.dingxinliang88.pojo.po.Song;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.pojo.vo.song.SongItemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Mapper
public interface SongMapper extends BaseMapper<Song> {
    /**
     * 修改歌曲信息（专辑信息）
     *
     * @param req 修改歌曲请求
     * @return true - 修改成功
     */
    Boolean editInfo(EditSongReq req);

    /**
     * 根据歌曲ID和乐队ID查找歌曲是否存在
     *
     * @param songId 歌曲ID
     * @param bandId 乐队ID
     * @return 歌曲信息
     */
    Song queryBySongIdAndBandIdInner(Integer songId, Integer bandId);

    /**
     * 根据乐队ID获取乐队相关的歌曲信息
     *
     * @param bandId 乐队ID
     * @return songs
     */
    List<Song> querySongsByBandId(Integer bandId);


    /**
     * 获取歌曲元素VO list
     *
     * @param bandId band id
     * @return song items vo list
     */
    List<SongItemVO> listSongItemsByBandId(Integer bandId);

    /**
     * 获取所有已经发布的歌曲信息
     *
     * @return song list
     */
    List<Song> listSongs();

    /**
     * 获取所有已经发布的歌曲信息（带乐队名称）
     *
     * @return song vo list
     */
    List<SongInfoVO> listSongInfoVO();

}
