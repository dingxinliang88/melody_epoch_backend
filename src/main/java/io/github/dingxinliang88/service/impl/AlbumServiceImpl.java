package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.mapper.AlbumMapper;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.mapper.SongMapper;
import io.github.dingxinliang88.pojo.dto.album.AddAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.EditAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.SongToAlbumReq;
import io.github.dingxinliang88.pojo.po.Album;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.service.AlbumService;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Album Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album>
        implements AlbumService {

    @Resource
    private SongMapper songMapper;

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private BandMapper bandMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public Integer addAlbum(AddAlbumReq req, HttpServletRequest request) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
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

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改专辑信息!");

        return albumMapper.editInfo(req);
    }

    @Override
    public List<AlbumInfoVO> listAlbumInfoVO(HttpServletRequest request) {
        return albumMapper.listAlbumInfoVO();
    }

    @Override
    public List<Album> currBandAllAlbums(HttpServletRequest request) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改专辑信息!");

        return albumMapper.queryAlbumByBandName(band.getName());
    }

    @Override
    public Boolean addSongsToAlbum(SongToAlbumReq req, HttpServletRequest request) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改专辑信息!");

        Album album = albumMapper.queryAlbumByAlbumId(req.getAlbumId());
        ThrowUtil.throwIf(!album.getBandName().equals(band.getName()), StatusCode.NO_AUTH_ERROR, "禁止的操作！");

        Integer albumId = req.getAlbumId();
        List<Integer> noneSelectedSongIds = req.getNoneSelectedSongIds();
        List<Integer> selectedSongIds = req.getSelectedSongIds();

        return transactionTemplate.execute(status -> {
            try {
                if (!noneSelectedSongIds.isEmpty()) {
                    songMapper.editBatchSongAlbumInfo(noneSelectedSongIds, null, null);
                }
                if (!selectedSongIds.isEmpty()) {
                    songMapper.editBatchSongAlbumInfo(selectedSongIds, albumId, album.getName());
                }
                return Boolean.TRUE;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }
}
