package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.dto.song.EditSongReq;
import io.github.dingxinliang88.pojo.po.Song;
import org.apache.ibatis.annotations.Mapper;

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
}
