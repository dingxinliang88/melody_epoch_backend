package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.album.AddAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.EditAlbumReq;
import io.github.dingxinliang88.pojo.po.Album;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Album Service
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public interface AlbumService extends IService<Album> {

    /**
     * 添加专辑
     *
     * @param req     添加专辑请求
     * @param request http request
     * @return 专辑ID
     */
    Integer addAlbum(AddAlbumReq req, HttpServletRequest request);

    /**
     * 修改专辑信息
     *
     * @param req     修改专辑请求
     * @param request http request
     * @return true - 修改成功
     */
    Boolean editInfo(EditAlbumReq req, HttpServletRequest request);

    /**
     * 获取已经发布的专辑信息VO
     *
     * @param request http request
     * @return album info vo
     */
    List<AlbumInfoVO> listAlbumInfoVO(HttpServletRequest request);
}
