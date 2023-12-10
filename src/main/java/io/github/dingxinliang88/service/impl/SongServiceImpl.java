package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.mapper.SongMapper;
import io.github.dingxinliang88.pojo.dto.song.AddSongReq;
import io.github.dingxinliang88.pojo.dto.song.EditSongReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.Song;
import io.github.dingxinliang88.pojo.vo.UserLoginVO;
import io.github.dingxinliang88.service.SongService;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    @Override
    public Integer addSong(AddSongReq req, HttpServletRequest request) {
        // 判断当前登录用户是否是乐队成员
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.MEMBER.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR, "暂无权限");

        // 判断是否是乐队队长
        Band band = bandMapper.queryByLeaderIdInner(currUser.getUserId());
        ThrowUtil.throwIf(null == band, StatusCode.NO_AUTH_ERROR, "暂无权限");

        Song song = new Song(req.getName(), band.getBandId(), req.getAuthor());
        songMapper.insert(song);

        return song.getSongId();
    }

    @Override
    public Boolean editInfo(EditSongReq req, HttpServletRequest request) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();

        Band band = bandMapper.queryByBandIdInner(req.getBandId());
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "乐队不存在");
        ThrowUtil.throwIf(!band.getLeaderId().equals(currUser.getUserId()), StatusCode.NO_AUTH_ERROR, "暂无权限");

        // 判断当前的歌曲信息是否存在
        Song song = songMapper.queryBySongIdAndBandIdInner(req.getSongId(), req.getBandId());
        ThrowUtil.throwIf(null == song, StatusCode.NOT_FOUND_ERROR, "歌曲信息不存在");

        return songMapper.editInfo(req);
    }
}
