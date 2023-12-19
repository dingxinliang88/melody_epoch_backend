package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.mapper.SongLikeMapper;
import io.github.dingxinliang88.mapper.SongMapper;
import io.github.dingxinliang88.pojo.dto.song.AddSongReq;
import io.github.dingxinliang88.pojo.dto.song.ReleaseSongReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.Song;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.pojo.vo.song.SongItemVO;
import io.github.dingxinliang88.pojo.vo.song.SongToAlbumVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.service.SongService;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Song Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class SongServiceImpl extends ServiceImpl<SongMapper, Song>
        implements SongService {

    @Resource
    private SongMapper songMapper;

    @Resource
    private BandMapper bandMapper;

    @Resource
    private SongLikeMapper songLikeMapper;

    @Override
    public Integer addSong(AddSongReq req, HttpServletRequest request) {
        // 判断当前登录用户是否是乐队成员
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.MEMBER.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR, "暂无权限");

        // 判断是否是乐队队长
        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(null == band, StatusCode.NO_AUTH_ERROR, "暂无权限");

        Song song = new Song(req.getName(), band.getBandId(), req.getAuthor());
        songMapper.insert(song);

        return song.getSongId();
    }

    @Override
    public List<SongItemVO> listSongItems(HttpServletRequest request) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "暂无权限");


        return songMapper.listSongItemsByBandId(band.getBandId());
    }

    @Override
    public List<SongInfoVO> listSongInfoVO(HttpServletRequest request) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        List<SongInfoVO> songInfoVOList = songMapper.listSongInfoVO();
        return songInfoVOList.stream()
                .peek(songInfoVO -> {
                    if (UserRoleType.FAN.getType().equals(currUser.getType())) {
                        songInfoVO.setCanLike(Boolean.TRUE);
                        songInfoVO.setIsLiked(songLikeMapper.queryBySongIdAndUserId(songInfoVO.getSongId(), currUser.getUserId()) != null);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public SongToAlbumVO listSongToAlbum(Integer albumId, HttpServletRequest request) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "暂无权限");

        List<SongItemVO> albumSongs = songMapper.queryCurrAlbumSongs(band.getBandId(), albumId);
        List<SongItemVO> noneAlbumSongs = songMapper.queryCurrAlbumSongs(band.getBandId(), null);

        return new SongToAlbumVO(noneAlbumSongs, albumSongs);
    }

    @Override
    public List<Song> currBandSongs(HttpServletRequest request) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "暂无权限");

        return songMapper.queryByBandId(band.getBandId());
    }

    @Override
    public Boolean releaseSong(ReleaseSongReq req, HttpServletRequest request) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "暂无权限");

        Integer songId = req.getSongId();
        Song song = songMapper.queryBySongId(songId, true);
        ThrowUtil.throwIf(!song.getBandId().equals(band.getBandId()), StatusCode.NOT_FOUND_ERROR, "暂无权限");

        return songMapper.updateReleaseStatusBySongId(songId, CommonConstant.RELEASE);
    }
}
