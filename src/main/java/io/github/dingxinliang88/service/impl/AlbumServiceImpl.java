package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.mapper.AlbumMapper;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.pojo.dto.album.AddAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.EditAlbumReq;
import io.github.dingxinliang88.pojo.po.Album;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.service.AlbumService;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Album Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album>
        implements AlbumService {

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private BandMapper bandMapper;

    @Override
    public Integer addAlbum(AddAlbumReq req, HttpServletRequest request) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderIdInner(userId);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法创建专辑!");

        Album album = new Album(req.getName(), req.getCompany(), band.getName(), req.getProfile());
        albumMapper.insert(album);

        return album.getAlbumId();
    }

    @Override
    public Boolean editInfo(EditAlbumReq req, HttpServletRequest request) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderIdInner(userId);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改专辑信息!");

        return albumMapper.editInfo(req);
    }
}
