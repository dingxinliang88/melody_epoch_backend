package io.github.dingxinliang88.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.AlbumConstant;
import io.github.dingxinliang88.constants.FanConstant;
import io.github.dingxinliang88.exception.BizException;
import io.github.dingxinliang88.mapper.*;
import io.github.dingxinliang88.pojo.dto.LikeReq;
import io.github.dingxinliang88.pojo.dto.fan.ScoreAlbumReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.*;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;
import io.github.dingxinliang88.pojo.vo.concert.ConcertInfoVO;
import io.github.dingxinliang88.pojo.vo.fan.FanInfoVO;
import io.github.dingxinliang88.pojo.vo.fan.LikeAlbumStatusVO;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.utils.RedisUtil;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static io.github.dingxinliang88.constants.CommonConstant.RELEASE;

/**
 * Fan Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Service
public class FanService extends ServiceImpl<FanMapper, Fan> {

    private final Logger logger = LoggerFactory.getLogger(FanService.class);

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private FanMapper fanMapper;

    @Resource
    private BandMapper bandMapper;

    @Resource
    private SongMapper songMapper;

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private ConcertMapper concertMapper;

    @Resource
    private BandLikeMapper bandLikeMapper;

    @Resource
    private AlbumLikeMapper albumLikeMapper;

    @Resource
    private SongLikeMapper songLikeMapper;

    private static final ExecutorService UPDATE_ALBUM_SCORE_THREAD_POOL = new ThreadPoolExecutor(
            1, 2, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100),
            r -> new Thread(r, "Album-Score-" + UUID.randomUUID().toString(true)), new ThreadPoolExecutor.AbortPolicy()
    );
    private static final int MAX_RETRIES = 3;

    /**
     * 喜欢收藏
     *
     * @param req 喜欢收藏请求
     * @return true - 喜欢成功
     */
    public Boolean like(LikeReq req) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR);

        Integer likeId = req.getLikeId();
        Integer type = req.getType();

        // 查询是否已经收藏
        switch (type) {
            case FanConstant.LIKE_BAND:
                ThrowUtil.throwIf(bandLikeMapper.queryByBandIdAndUserId(likeId, currUser.getUserId()) != null,
                        StatusCode.DUPLICATE_DATA, "已经喜欢过了");
                BandLike bandLike = new BandLike();
                bandLike.setBandId(likeId);
                bandLike.setUserId(currUser.getUserId());
                bandLikeMapper.insert(bandLike);
                break;
            case FanConstant.LIKE_ALBUM:
                ThrowUtil.throwIf(albumLikeMapper.queryByAlbumIdAndUserId(likeId, currUser.getUserId()) != null,
                        StatusCode.DUPLICATE_DATA, "已经喜欢过了");
                AlbumLike albumLike = new AlbumLike();
                albumLike.setAlbumId(likeId);
                albumLike.setUserId(currUser.getUserId());
                albumLikeMapper.insert(albumLike);
                break;
            case FanConstant.LIKE_SONG:
                ThrowUtil.throwIf(songLikeMapper.queryBySongIdAndUserId(likeId, currUser.getUserId()) != null,
                        StatusCode.DUPLICATE_DATA, "已经喜欢过了");
                SongLike songLike = new SongLike();
                songLike.setSongId(likeId);
                songLike.setUserId(currUser.getUserId());
                songLikeMapper.insert(songLike);
                break;
            default:
                throw new BizException(StatusCode.PARAMS_ERROR);
        }
        return Boolean.TRUE;
    }

    /**
     * 撤销喜欢收藏
     *
     * @param req 撤销喜欢收藏请求
     * @return true - 撤销成功
     */
    public Boolean unlike(LikeReq req) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR);

        Integer likeId = req.getLikeId();
        Integer type = req.getType();

        // 查询是否已经收藏
        switch (type) {
            case FanConstant.LIKE_BAND:
                ThrowUtil.throwIf(bandLikeMapper.queryByBandIdAndUserId(likeId, currUser.getUserId()) == null,
                        StatusCode.DUPLICATE_DATA, "尚未喜欢过");
                return bandLikeMapper.deleteByBandIdAndUserId(likeId, currUser.getUserId());
            case FanConstant.LIKE_ALBUM:
                ThrowUtil.throwIf(albumLikeMapper.queryByAlbumIdAndUserId(likeId, currUser.getUserId()) == null,
                        StatusCode.DUPLICATE_DATA, "尚未喜欢过");
                return albumLikeMapper.deleteByAlbumIdAndUserId(likeId, currUser.getUserId());
            case FanConstant.LIKE_SONG:
                ThrowUtil.throwIf(songLikeMapper.queryBySongIdAndUserId(likeId, currUser.getUserId()) == null,
                        StatusCode.DUPLICATE_DATA, "尚未喜欢过");
                return songLikeMapper.deleteBySongIdAndUserId(likeId, currUser.getUserId());
            default:
                throw new BizException(StatusCode.PARAMS_ERROR);
        }

    }

    /**
     * 给专辑打分请求
     *
     * @param req 打分请求
     * @return true - 打分成功
     */
    public Boolean scoreAlbum(ScoreAlbumReq req) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR);

        final Integer albumId = req.getAlbumId();
        AlbumLike albumLike = albumLikeMapper.queryByAlbumIdAndUserId(albumId, currUser.getUserId());
        ThrowUtil.throwIf(albumLike == null, StatusCode.NOT_FOUND_ERROR, "该专辑还未被喜欢");
        ThrowUtil.throwIf(albumLike.getScore() != 0, StatusCode.DUPLICATE_DATA, "已经评分过了");

        Boolean res = albumLikeMapper.updateScore(req.getScore(), albumId, currUser.getUserId());
        // 更新 打分次数
        redisUtil.increment(AlbumConstant.TOP_ALBUM_CACHE_EPOCH_KEY, 1);
        if (res) {
            asyncUpdateAlbumAvgScore(albumId);
        }
        return res;
    }

    /**
     * 获取喜欢专辑的状态
     *
     * @param albumId 专辑ID
     * @return 喜欢专辑状态
     */
    public LikeAlbumStatusVO getLikeAlbumStatus(Integer albumId) {
        UserLoginVO currUser = SysUtil.getCurrUser();

        LikeAlbumStatusVO likeAlbumStatusVO = new LikeAlbumStatusVO();

        if (!UserRoleType.FAN.getType().equals(currUser.getType())) return likeAlbumStatusVO;

        AlbumLike albumLike = albumLikeMapper.queryByAlbumIdAndUserId(albumId, currUser.getUserId());
        likeAlbumStatusVO.setCanLike(Boolean.TRUE);
        likeAlbumStatusVO.setIsLiked(albumLike != null);
        if (albumLike != null)
            likeAlbumStatusVO.setIsScored(albumLike.getScore() != 0);
        return likeAlbumStatusVO;
    }

    /**
     * 查询我喜欢的乐队
     *
     * @return like item list
     */
    public List<BandInfoVO> listMyLikedBand() {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR);
        List<Band> bands = bandLikeMapper.listMyLikedBand(currUser.getUserId());

        // 获取队长姓名
        return bands.stream().map(band -> {
            String leaderName = memberMapper.queryNameByMemberId(band.getLeaderId());
            BandLike bandLike = bandLikeMapper.queryByBandIdAndUserId(band.getBandId(), SysUtil.getCurrUser().getUserId());
            return new BandInfoVO(band.getBandId(), band.getName(), band.getFoundTime(),
                    leaderName, band.getMemberNum(), bandLike != null);
        }).collect(Collectors.toList());
    }

    /**
     * 分页查询我喜欢的乐队
     *
     * @return like item list
     */
    public Page<BandInfoVO> listMyLikedBandByPage(Integer current, Integer size) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR);

        LambdaQueryWrapper<Band> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.inSql(Band::getBandId, "SELECT band_id FROM band_like\n" +
                        "WHERE user_id = " + currUser.getUserId())
                .eq(Band::getIsRelease, RELEASE);
        Page<Band> bandPage = bandMapper.selectPage(new Page<>(current, size), queryWrapper);

        return convertBandInfoVOPage(bandPage);
    }


    /**
     * 查询我喜欢的专辑
     *
     * @return like item list
     */
    public List<AlbumInfoVO> listMyLikedAlbum() {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR);
        List<AlbumInfoVO> albums = albumLikeMapper.listMyLikedAlbum(currUser.getUserId());
        return albums.stream()
                .peek(albumInfoVO -> {
                    albumInfoVO.setCanLike(Boolean.TRUE);
                    albumInfoVO.setIsLiked(Boolean.TRUE);
                })
                .collect(Collectors.toList());
    }

    public Page<AlbumInfoVO> listMyLikedAlbumByPage(Integer current, Integer size) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR);

        LambdaQueryWrapper<Album> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.inSql(Album::getAlbumId, "SELECT album_id FROM album_like WHERE user_id = " + currUser.getUserId())
                .eq(Album::getIsRelease, RELEASE);
        Page<Album> albumPage = albumMapper.selectPage(new Page<>(current, size), queryWrapper);

        return convertAlbumInfoVOPage(albumPage);
    }


    /**
     * 查询我喜欢的歌曲
     *
     * @return like item list
     */
    public List<SongInfoVO> listMyLikedSong() {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR);
        List<SongInfoVO> songInfoVOList = songLikeMapper.listMyLikedSong(currUser.getUserId());
        return songInfoVOList.stream()
                .peek(songInfoVO ->
                        songInfoVO.setIsLiked(true))
                .collect(Collectors.toList());
    }


    /**
     * 分页查询我喜欢的歌曲
     *
     * @return like item list
     */
    public Page<SongInfoVO> listMyLikedSongByPage(Integer current, Integer size) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR);

        LambdaQueryWrapper<Song> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.inSql(Song::getSongId, "SELECT song_id FROM song_like WHERE user_id = " + currUser.getUserId())
                .eq(Song::getIsRelease, RELEASE);
        Page<Song> songPage = songMapper.selectPage(new Page<>(current, size), queryWrapper);

        return convertSongInfoVOPage(songPage);
    }

    /**
     * 分页查询我加入的演唱会
     *
     * @return concert info vo page
     */
    public Page<ConcertInfoVO> listMyJoinedConcertByPage(Integer current, Integer size) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR);

        LambdaQueryWrapper<Concert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.inSql(Concert::getConcertId, "SELECT concert_id FROM concert_join WHERE user_id = " + currUser.getUserId())
                .eq(Concert::getIsRelease, RELEASE);
        Page<Concert> concertPage = concertMapper.selectPage(new Page<>(current, size), queryWrapper);

        return convertConcertInfoVOPage(concertPage);
    }

    /**
     * 分页查看乐队的歌迷
     *
     * @param bandId  band id
     * @param current 页码
     * @param size    每页数据量
     * @return fan info vo page
     */
    public Page<FanInfoVO> getLikedFanByBandIdAndPage(Integer bandId, Integer current, Integer size) {
        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(user.getUserId(), false);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止该操作");

        /*
            SELECT *
            FROM fan
            WHERE fan_id IN (
                SELECT user_id
                FROM band_like
                WHERE band_id = 10
            )
         */
        LambdaQueryWrapper<Fan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.inSql(Fan::getFanId, "SELECT user_id FROM band_like WHERE band_id = " + bandId);
        Page<Fan> fanPage = fanMapper.selectPage(new Page<>(current, size), queryWrapper);

        return convertFanInfoVOPage(fanPage);
    }

    /**
     * 分页查看专辑的歌迷
     *
     * @param albumId album id
     * @param current 页码
     * @param size    每页数据量
     * @return fan info vo page
     */
    public Page<FanInfoVO> getAlbumFansByBandIdAndPage(Integer albumId, Integer current, Integer size) {
        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(user.getUserId(), false);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止该操作");

        LambdaQueryWrapper<Fan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.inSql(Fan::getFanId, "SELECT user_id FROM album_like WHERE album_id = " + albumId);
        Page<Fan> fanPage = fanMapper.selectPage(new Page<>(current, size), queryWrapper);

        return convertFanInfoVOPage(fanPage);
    }

    /**
     * 分页查看歌曲的歌迷
     *
     * @param songId  song id
     * @param current 页码
     * @param size    每页数据量
     * @return fan info vo page
     */
    public Page<FanInfoVO> getSongFansByBandIdAndPage(Integer songId, Integer current, Integer size) {
        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(user.getUserId(), false);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止该操作");

        LambdaQueryWrapper<Fan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.inSql(Fan::getFanId, "SELECT user_id FROM song_like WHERE song_id = " + songId);
        Page<Fan> fanPage = fanMapper.selectPage(new Page<>(current, size), queryWrapper);

        return convertFanInfoVOPage(fanPage);
    }

    /**
     * 分页查看参加演唱会的歌迷
     *
     * @param concertId concert id
     * @param current   页码
     * @param size      每页数据量
     * @return fan info vo page
     */
    public Page<FanInfoVO> getConcertFansByBandIdAndPage(Long concertId, Integer current, Integer size) {
        // 获取当前登录用户，判断是否是队长
        UserLoginVO user = SysUtil.getCurrUser();

        Band band = bandMapper.queryByLeaderId(user.getUserId(), false);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "禁止该操作");

        LambdaQueryWrapper<Fan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.inSql(Fan::getFanId, "SELECT user_id FROM concert_join WHERE concert_id = " + concertId);
        Page<Fan> fanPage = fanMapper.selectPage(new Page<>(current, size), queryWrapper);

        return convertFanInfoVOPage(fanPage);
    }


    // ---------------------------------
    // private util function
    // ---------------------------------
    private void asyncUpdateAlbumAvgScore(Integer albumId) {
        CompletableFuture.runAsync(() -> {
            int retryCount = 0;
            boolean updateRes = false;

            while (retryCount < MAX_RETRIES) {
                List<AlbumLike> albumLikeList = albumLikeMapper.queryByAlbumId(albumId);
                updateRes = updateAlbumScore(albumLikeList, albumId);

                if (updateRes) {
                    // 如果更新成功，跳出循环
                    break;
                }

                // 更新失败，等待一段时间后进行重试
                retryCount++;
                try {
                    Thread.sleep(1000); // 休眠1秒钟
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (!updateRes) {
                logger.error("Max retry count reached. Unable to update album score.");
            }
        }, UPDATE_ALBUM_SCORE_THREAD_POOL);
    }

    /*
        UPDATE album
        SET avg_score = (
            SELECT AVG(score) AS average_score
            FROM album_like
            WHERE album_like.album_id = album.album_id
                AND album_like.score <> 0
            GROUP BY album_like.album_id
        )
        WHERE album_id = 1;
     */
    private boolean updateAlbumScore(List<AlbumLike> albumLikeList, Integer albumId) {
        List<AlbumLike> nonZeroScoreList = albumLikeList.stream()
                .filter(albumLike -> albumLike.getScore() != 0)
                .collect(Collectors.toList());
        if (!nonZeroScoreList.isEmpty()) {
            // 计算非零评分的平均值
            double avgScore = nonZeroScoreList.stream().mapToDouble(AlbumLike::getScore).average().orElse(0.0);
            // 更新数据库
            return albumMapper.updateAvgScore(albumId, avgScore);
        }
        return true;
    }

    private Page<BandInfoVO> convertBandInfoVOPage(Page<Band> bandPage) {
        Page<BandInfoVO> bandInfoVOPage = new Page<>(bandPage.getCurrent(), bandPage.getSize(), bandPage.getTotal(), bandPage.searchCount());

        List<BandInfoVO> bandInfoVOList = bandPage.getRecords().stream().map(band -> {
            String leaderName = memberMapper.queryNameByMemberId(band.getLeaderId());
            BandLike bandLike = bandLikeMapper.queryByBandIdAndUserId(band.getBandId(), SysUtil.getCurrUser().getUserId());
            return new BandInfoVO(band.getBandId(), band.getName(), band.getFoundTime(),
                    leaderName, band.getMemberNum(), bandLike != null);
        }).collect(Collectors.toList());
        bandInfoVOPage.setRecords(bandInfoVOList);

        return bandInfoVOPage;
    }

    private Page<AlbumInfoVO> convertAlbumInfoVOPage(Page<Album> albumPage) {
        Page<AlbumInfoVO> albumInfoVOPage = new Page<>(albumPage.getCurrent(), albumPage.getSize(), albumPage.getTotal(), albumPage.searchCount());

        List<AlbumInfoVO> albumInfoVOList = albumPage.getRecords().stream().map(album -> {
            AlbumInfoVO albumInfoVO = AlbumInfoVO.albumToVO(album);
            albumInfoVO.setCanLike(Boolean.TRUE);
            albumInfoVO.setIsLiked(Boolean.TRUE);
            return albumInfoVO;
        }).collect(Collectors.toList());
        albumInfoVOPage.setRecords(albumInfoVOList);
        return albumInfoVOPage;
    }

    private Page<SongInfoVO> convertSongInfoVOPage(Page<Song> songPage) {
        Page<SongInfoVO> songInfoVOPage = new Page<>(songPage.getCurrent(), songPage.getSize(), songPage.getTotal(), songPage.searchCount());

        Map<Integer, String> bandNameMap = new HashMap<>(16);
        List<SongInfoVO> songInfoVOList = songPage.getRecords().stream().map(song -> {
            SongInfoVO songInfoVO = SongInfoVO.songToVO(song);
            Integer bandId = song.getBandId();
            if (!bandNameMap.containsKey(bandId)) {
                Band band = bandMapper.queryByBandId(bandId, true);
                bandNameMap.put(bandId, band.getName());
            }
            songInfoVO.setBandName(bandNameMap.get(bandId));
            songInfoVO.setCanLike(Boolean.TRUE);
            songInfoVO.setIsLiked(Boolean.TRUE);
            return songInfoVO;
        }).collect(Collectors.toList());
        songInfoVOPage.setRecords(songInfoVOList);

        return songInfoVOPage;
    }

    private Page<ConcertInfoVO> convertConcertInfoVOPage(Page<Concert> concertPage) {
        Page<ConcertInfoVO> concertInfoVOPage = new Page<>(concertPage.getCurrent(), concertPage.getSize(), concertPage.getTotal(), concertPage.searchCount());
        List<ConcertInfoVO> concertInfoVOList = concertPage.getRecords().stream().map(ConcertInfoVO::concertToVO).collect(Collectors.toList());
        concertInfoVOPage.setRecords(concertInfoVOList);
        return concertInfoVOPage;
    }

    private Page<FanInfoVO> convertFanInfoVOPage(Page<Fan> fanPage) {
        Page<FanInfoVO> fanInfoVOPage = new Page<>(fanPage.getCurrent(), fanPage.getSize(), fanPage.getTotal(), fanPage.searchCount());
        List<FanInfoVO> fanInfoVOList = fanPage.getRecords().stream().map(FanInfoVO::fanToVO).collect(Collectors.toList());
        fanInfoVOPage.setRecords(fanInfoVOList);

        return fanInfoVOPage;
    }


}
