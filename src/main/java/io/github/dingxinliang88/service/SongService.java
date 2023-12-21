package io.github.dingxinliang88.service;

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
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Song Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class SongService extends ServiceImpl<SongMapper, Song> {

    @Resource
    private SongMapper songMapper;

    @Resource
    private BandMapper bandMapper;

    @Resource
    private SongLikeMapper songLikeMapper;

    /**
     * 添加歌曲信息
     *
     * @param req     添加歌曲请求
     * @return 歌曲ID
     */
    public Integer addSong(AddSongReq req) {
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

    /**
     * 获取歌曲元素VO
     *
     * @return song items vo list
     */
    public List<SongItemVO> listSongItems() {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "暂无权限");


        return songMapper.listSongItemsByBandId(band.getBandId());
    }

    /**
     * 获取已经发布的歌曲信息
     *
     * @return song info vo list
     */
    public List<SongInfoVO> listSongInfoVO() {
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

    /**
     * 查询已经录入当前专辑的歌曲信息和未录入专辑的歌曲信息
     *
     * @param albumId album id
     * @return song to album vo
     */
    public SongToAlbumVO listSongToAlbum(Integer albumId) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "暂无权限");

        List<SongItemVO> albumSongs = songMapper.queryCurrAlbumSongs(band.getBandId(), albumId);
        List<SongItemVO> noneAlbumSongs = songMapper.queryCurrAlbumSongs(band.getBandId(), null);

        return new SongToAlbumVO(noneAlbumSongs, albumSongs);
    }

    /**
     * 查询当前乐队的歌曲信息
     *
     * @return song info
     */
    public List<Song> currBandSongs() {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(currUser.getUserId(), true);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "暂无权限");

        return songMapper.queryByBandId(band.getBandId());
    }

    /**
     * 发布歌曲信息
     *
     * @param req     发布歌曲信息请求
     * @return true - 发布成功
     */
    public Boolean releaseSong(ReleaseSongReq req) {
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
