package io.github.dingxinliang88.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.FanConstant;
import io.github.dingxinliang88.exception.BizException;
import io.github.dingxinliang88.mapper.*;
import io.github.dingxinliang88.pojo.dto.LikeReq;
import io.github.dingxinliang88.pojo.dto.fan.EditFanReq;
import io.github.dingxinliang88.pojo.dto.fan.ScoreAlbumReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.*;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;
import io.github.dingxinliang88.pojo.vo.fan.LikeAlbumStatusVO;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Fan Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class FanService extends ServiceImpl<FanMapper, Fan> {

    private final Logger logger = LoggerFactory.getLogger(FanService.class);

    @Resource
    private FanMapper fanMapper;

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private BandLikeMapper bandLikeMapper;

    @Resource
    private AlbumLikeMapper albumLikeMapper;

    @Resource
    private SongLikeMapper songLikeMapper;

    private static final ExecutorService UPDATE_ALBUM_SCORE_THREAD_POOL = new ThreadPoolExecutor(
            1, 2, 5000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100),
            r -> new Thread(r, "Album-Score-" + UUID.randomUUID().toString(true)), new ThreadPoolExecutor.AbortPolicy()
    );
    private static final int MAX_RETRIES = 3;


    /**
     * 乐迷修改自己的信息
     *
     * @param req     修改信息请求
     * @return true - 修改成功
     */
    public Boolean editInfo(EditFanReq req) {

        Integer fanId = req.getFanId();

        // 检查是否是本人
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!currUser.getUserId().equals(fanId), StatusCode.NO_AUTH_ERROR,
                "无权修改其他乐迷信息！");

        // 查找相关的成员是否存在
        Fan fan = fanMapper.queryByFanId(fanId);
        ThrowUtil.throwIf(fan == null, StatusCode.NOT_FOUND_ERROR, "未查找到相关成员信息！");

        // 更新相关的信息
        return fanMapper.updateInfoByFanId(req);
    }

    /**
     * 喜欢收藏
     *
     * @param req     喜欢收藏请求
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
     * @param req     撤销喜欢收藏请求
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
     * @param req     打分请求
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
     * 查询我喜欢的专辑
     *
     * @return like item list
     */
    public List<AlbumInfoVO> listMyLikedAlbum() {
        UserLoginVO currUser = SysUtil.getCurrUser();
        ThrowUtil.throwIf(!UserRoleType.FAN.getType().equals(currUser.getType()), StatusCode.NO_AUTH_ERROR);
        List<AlbumInfoVO> albums = albumLikeMapper.listMyLikedAlbum(currUser.getUserId());
        return albums.stream()
                .peek(albumInfoVO -> albumInfoVO.setIsLiked(true))
                .collect(Collectors.toList());
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
}
