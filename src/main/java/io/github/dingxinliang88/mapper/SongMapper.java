package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    List<SongInfoVO> querySongsByBandId(Integer bandId);

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

    /**
     * 查询乐队当前专辑的歌曲
     *
     * @param bandId  band id
     * @param albumId album id
     * @return song item vo list
     */
    List<SongItemVO> queryCurrAlbumSongs(Integer bandId, Integer albumId);

    /**
     * 批量修改歌曲专辑信息
     *
     * @param songIds   song id list
     * @param albumId   album id
     * @param albumName album name
     * @return true - 修改成功
     */
    Boolean editBatchSongAlbumInfo(List<Integer> songIds, Integer albumId, String albumName);

    /**
     * 批量查询歌曲信息
     *
     * @param albumId 专辑ID
     * @return song info vo list
     */
    List<SongInfoVO> querySongInfoVOByAlbumId(Integer albumId);

    /**
     * 根据乐队ID查询歌曲信息
     *
     * @param bandId band id
     * @return song list
     */
    List<Song> queryByBandId(Integer bandId);

    /**
     * 批量发布歌曲信息
     *
     * @param songIds song id list
     * @param release release status
     * @return true - 发布成功
     */
    Boolean updateBatchReleaseStatus(List<Integer> songIds, Integer release);

    /**
     * 查询歌曲信息
     *
     * @param songId song id
     * @param inner  是否是内部接口
     * @return song info
     */
    Song queryBySongId(Integer songId, boolean inner);

    /**
     * 修改单个歌曲发布状态
     *
     * @param songId  song id
     * @param release release status
     * @return true - 修改成功
     */
    Boolean updateReleaseStatusBySongId(Integer songId, Integer release);

    /**
     * 修改歌曲发布状态
     *
     * @param bandId  band id
     * @param release release status
     * @return true - 修改成功
     */
    Boolean updateReleaseStatusByBandId(Integer bandId, Integer release);

    /**
     * 批量查询
     *
     * @param songIds song ids
     * @return song info vo list
     */
    List<SongInfoVO> queryBatchBySongId(List<Integer> songIds);

}
