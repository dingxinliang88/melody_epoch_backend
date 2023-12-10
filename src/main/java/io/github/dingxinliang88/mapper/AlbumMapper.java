package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.dto.album.EditAlbumReq;
import io.github.dingxinliang88.pojo.po.Album;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Mapper
public interface AlbumMapper extends BaseMapper<Album> {

    /**
     * 修改专辑信息
     *
     * @param req 修改专辑请求
     * @return true - 修改成功
     */
    Boolean editInfo(EditAlbumReq req);
}
