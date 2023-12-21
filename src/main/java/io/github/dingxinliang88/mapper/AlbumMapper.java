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
     * 根据乐队名称查询对应的专辑信息
     *
     * @param bandName 乐队名称
     * @return album list
     */
    List<AlbumInfoVO> queryAlbumByBandName(String bandName);

    /**
     * 根据专辑ID查询对应的专辑信息
     *
     * @param albumId 专辑名称
     * @param inner   是否是内部专用
     * @return album info
     */
    Album queryAlbumByAlbumId(Integer albumId, boolean inner);

    /**
     * 发布专辑信息
     *
     * @param albumId 发布专辑信息
     * @param release release status
     * @return true - 发布成功
     */
    Boolean updateAlbumReleaseStatusByAlbumId(Integer albumId, Integer release);

    /**
     * 发布专辑信息
     *
     * @param bandName band name
     * @param release  release status
     * @return true - 发布成功
     */
    Boolean updateAlbumReleaseStatusByBandName(String bandName, Integer release);

    /**
     * 更新专辑均分
     *
     * @param albumId  album id
     * @param avgScore avg score
     * @return true - 更新成功
     */
    boolean updateAvgScore(Integer albumId, double avgScore);
}
