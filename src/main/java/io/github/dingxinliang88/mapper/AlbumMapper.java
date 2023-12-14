package io.github.dingxinliang88.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.dingxinliang88.pojo.dto.album.EditAlbumReq;
import io.github.dingxinliang88.pojo.po.Album;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    /**
     * 获取所有的已经发布的专辑信息VO
     *
     * @return album info vo
     */
    List<AlbumInfoVO> listAlbumInfoVO();

    /**
     * 发布专辑信息
     *
     * @param bandName band name
     */
    Boolean releaseAlbumInfo(String bandName);


    /**
     * 根据乐队名称查询对应的专辑信息
     *
     * @param bandName 乐队名称
     * @return album list
     */
    List<Album> queryAlbumByBandName(String bandName);

    /**
     * 根据专辑ID查询对应的专辑信息
     *
     * @param albumId 专辑名称
     * @return album info
     */
    Album queryAlbumByAlbumId(Integer albumId);
}
