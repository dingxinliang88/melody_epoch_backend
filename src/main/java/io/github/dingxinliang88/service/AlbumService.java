package io.github.dingxinliang88.service;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.AlbumConstant;
import io.github.dingxinliang88.manager.SensitiveHandler;
import io.github.dingxinliang88.mapper.*;
import io.github.dingxinliang88.pojo.dto.album.AddAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.EditAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.ReleaseAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.SongToAlbumReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Album;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.Comment;
import io.github.dingxinliang88.pojo.vo.album.AlbumDetailsVO;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import io.github.dingxinliang88.pojo.vo.album.TopAlbumVO;
import io.github.dingxinliang88.pojo.vo.comment.CommentVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.utils.ContentUtil;
import io.github.dingxinliang88.utils.RedisUtil;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static io.github.dingxinliang88.constants.CommonConstant.*;

/**
 * Album Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Service
public class AlbumService extends ServiceImpl<AlbumMapper, Album> {

    @Resource
    private UserMapper userMapper;
    @Resource
    private SongMapper songMapper;

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private BandMapper bandMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private AlbumLikeMapper albumLikeMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private SensitiveHandler sensitiveHandler;

    @Resource
    private TransactionTemplate transactionTemplate;

    private static final ExecutorService CACHE_TOP_ALBUMS_THREAD_POOL = new ThreadPoolExecutor(
            1, 2, 5000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(50),
            r -> new Thread(r, "Top-Album-" + UUID.randomUUID().toString(true)), new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * 添加专辑
     *
     * @param req 添加专辑请求
     * @return 专辑ID
     */
    public Integer addAlbum(AddAlbumReq req) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法创建专辑!");

        Album album = new Album(req.getName(), req.getCompany(), band.getName(), req.getProfile());
        albumMapper.insert(album);

        return album.getAlbumId();
    }

    /**
     * 修改专辑信息
     *
     * @param req 修改专辑请求
     * @return true - 修改成功
     */
    public Boolean editAlbumInfo(EditAlbumReq req) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改专辑信息!");

        // 过滤简介
        String profile = req.getProfile();
        String cleanProfile = ContentUtil.cleanContent(profile);
        req.setProfile(cleanProfile);

        // 触发敏感词计数器
        sensitiveHandler.handleAccSensitive(userId, !profile.equals(cleanProfile));

        return albumMapper.editInfo(req);
    }

    /**
     * 获取已经发布的专辑信息VO
     *
     * @return album info vo
     */
    public List<AlbumInfoVO> listAlbumInfo() {
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();
        List<AlbumInfoVO> albumInfoVOList = albumMapper.listAlbumInfoVO();
        return albumInfoVOList.stream()
                .peek(albumInfoVO -> {
                    if (UserRoleType.FAN.getType().equals(currUser.getType())) {
                        albumInfoVO.setCanLike(Boolean.TRUE);
                        albumInfoVO.setIsLiked(albumLikeMapper.queryByAlbumIdAndUserId(albumInfoVO.getAlbumId(), userId) != null);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 分页获取已经发布的专辑信息VO
     *
     * @param current 页码
     * @return album info vo
     */
    public Page<AlbumInfoVO> listAlbumInfoByPage(Integer current) {
        LambdaQueryWrapper<Album> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Album::getIsRelease, RELEASE);

        Page<Album> albumPage = albumMapper.selectPage(new Page<>(current, DEFAULT_PAGE_SIZE), queryWrapper);

        return convertAlbumInfoVOPage(albumPage, false);
    }


    /**
     * 获取当前乐队所有的专辑信息
     *
     * @return album list
     */
    public List<AlbumInfoVO> getCurrBandAllAlbums() {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改专辑信息!");

        return albumMapper.queryAlbumByBandName(band.getName());
    }

    /**
     * 分页获取当前乐队所有的专辑信息
     *
     * @param current 页码
     * @return album list
     */
    public Page<AlbumInfoVO> getCurrBandAllAlbumsByPage(Integer current, Integer size) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "暂无权限！");
        LambdaQueryWrapper<Album> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Album::getBandName, band.getName());
        Page<Album> albumPage = albumMapper.selectPage(new Page<>(current, size), queryWrapper);

        return convertAlbumInfoVOPage(albumPage, true);
    }

    /**
     * 分页获取指定乐队所有的专辑信息
     *
     * @param bandId  band id
     * @param current 页码
     * @return album list
     */
    public Page<AlbumInfoVO> getBandAlbumsByPage(Integer bandId, Integer current, Integer size) {
        Band band = bandMapper.queryByBandId(bandId, false);
        ThrowUtil.throwIf(band == null, StatusCode.NOT_FOUND_ERROR, "查询无果！");
        LambdaQueryWrapper<Album> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Album::getBandName, band.getName());
        Page<Album> albumPage = albumMapper.selectPage(new Page<>(current, size), queryWrapper);

        return convertAlbumInfoVOPage(albumPage, true);
    }

    /**
     * 歌曲录入专辑
     *
     * @param req 歌曲录入专辑的请求
     * @return true - 录入成功
     */
    public Boolean addSongsToAlbum(SongToAlbumReq req) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "您不是乐队队长，无法修改专辑信息!");

        Album album = albumMapper.queryAlbumByAlbumId(req.getAlbumId(), true);
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

    /**
     * 获取专辑详细信息
     *
     * @param albumId 专辑ID
     * @return 专辑详细信息
     */
    public AlbumDetailsVO getAlbumDetailsInfo(Integer albumId) {
        // 获取专辑详细信息
        Album album = albumMapper.queryAlbumByAlbumId(albumId, true);
        ThrowUtil.throwIf(album == null, StatusCode.NOT_FOUND_ERROR, "查询无果！");
        AlbumDetailsVO albumDetailsVO = new AlbumDetailsVO(album);
        // 获取专辑的评论信息
        List<Comment> comments = commentMapper.queryByAlbumId(albumId);
        List<CommentVO> commentVOList = parseComments(comments);
        albumDetailsVO.setCommentVOList(commentVOList);

        // 判断当前用户是否可以评论
        UserLoginVO currUser = SysUtil.getCurrUser();
        albumDetailsVO.setCanComment(UserRoleType.FAN.getType().equals(currUser.getType()));

        return albumDetailsVO;
    }

    /**
     * 发布专辑信息
     *
     * @param req 发布专辑信息
     * @return true - 发布成功
     */
    public Boolean releaseAlbum(ReleaseAlbumReq req) {
        // 判断当前登录用户是否是乐队队长
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();

        Band band = bandMapper.queryByLeaderId(userId, true);
        ThrowUtil.throwIf(band == null, StatusCode.NO_AUTH_ERROR, "暂无权限！");

        Integer albumId = req.getAlbumId();
        Album album = albumMapper.queryAlbumByAlbumId(albumId, true);
        ThrowUtil.throwIf(album == null, StatusCode.NOT_FOUND_ERROR, "查询无果！");
        ThrowUtil.throwIf(!album.getBandName().equals(band.getName()), StatusCode.NO_AUTH_ERROR, "暂无权限！");

        return transactionTemplate.execute(status -> {
            try {
                String songIdsStr = album.getSongIdsStr();
                if (StrUtil.isNotEmpty(songIdsStr)) {
                    List<Integer> songIds = Arrays.stream(songIdsStr.split(SONGS_STR_SEPARATOR))
                            .map(Integer::parseInt).collect(Collectors.toList());
                    songMapper.updateBatchReleaseStatus(songIds, RELEASE);
                }
                return albumMapper.updateAlbumReleaseStatusByAlbumId(albumId, RELEASE);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 获取 TopN 专辑
     *
     * @return topN 专辑
     */
    public List<TopAlbumVO> topAlbums() {
        // 尝试从缓存中获取数据
        Object topAlbumsObj = redisUtil.get(AlbumConstant.TOP_ALBUMS_KEY);
        if (topAlbumsObj != null) {
            String topAlbumsJson = topAlbumsObj.toString();
            return JSONUtil.toBean(topAlbumsJson, new TypeReference<List<TopAlbumVO>>() {
            }, false);
        }

        // 从数据库中查询
        List<TopAlbumVO> topAlbumVOList = albumMapper.queryTopAlbums(AlbumConstant.TopN);

        // 存入缓存
        CompletableFuture.runAsync(() -> {
            save2Cache(topAlbumVOList);
        }, CACHE_TOP_ALBUMS_THREAD_POOL);

        return topAlbumVOList;
    }

    // -------------------------------------
    // private util functions
    // -------------------------------------

    private List<CommentVO> parseComments(List<Comment> comments) {
        if (comments.isEmpty()) return null;

        // Step 1: Create a map to store comments grouped by parent comment ID
        Map<Integer, List<Comment>> groupedComments = comments.stream()
                .collect(Collectors.groupingBy(Comment::getParentId));

        // Step 2: Process top-level comments and create CommentVO objects
        List<CommentVO> result = new ArrayList<>();
        List<Comment> topLevelComments = groupedComments.getOrDefault(0, Collections.emptyList());

        for (Comment topLevelComment : topLevelComments) {
            CommentVO commentVO = convertToCommentVO(topLevelComment);
            processChildComments(commentVO, groupedComments);
            result.add(commentVO);
        }

        // Step 3: Sort the top-level comments based on creation time
        result.sort(Comparator.comparing(CommentVO::getCreateTime, Comparator.reverseOrder()));
        return result;
    }

    private CommentVO convertToCommentVO(Comment comment) {
        // Fetch user name from user table using userId
        String userName = userMapper.queryNickNameByUserId(comment.getUserId());

        // Create CommentVO
        CommentVO commentVO = new CommentVO();
        commentVO.setCommentId(comment.getCommentId());
        commentVO.setAlbumId(comment.getAlbumId());
        commentVO.setContent(comment.getContent());
        commentVO.setParentId(comment.getParentId());
        commentVO.setUserId(comment.getUserId());
        commentVO.setUserName(userName);
        commentVO.setCreateTime(comment.getCreateTime());
        return commentVO;
    }

    private void processChildComments(CommentVO parentCommentVO, Map<Integer, List<Comment>> groupedComments) {
        List<Comment> childComments = groupedComments.getOrDefault(parentCommentVO.getCommentId(), Collections.emptyList());
        // Sort child comments based on creation time in descending order
        childComments.sort(Comparator.comparing(Comment::getCreateTime, Comparator.reverseOrder()));
        for (Comment childComment : childComments) {
            CommentVO childCommentVO = convertToCommentVO(childComment);
            processChildComments(childCommentVO, groupedComments);
            // Initialize the childrenComments list if it's null
            if (parentCommentVO.getChildrenComments() == null) {
                parentCommentVO.setChildrenComments(new ArrayList<>());
            }
            parentCommentVO.getChildrenComments().add(childCommentVO);
        }
    }

    private void save2Cache(List<TopAlbumVO> topAlbumVOList) {
        String topJson = JSONUtil.toJsonStr(topAlbumVOList);
        redisUtil.setExpiredHours(AlbumConstant.TOP_ALBUMS_KEY, topJson, AlbumConstant.TOP_ALBUMS_EXPIRE_TIME);
    }

    private Page<AlbumInfoVO> convertAlbumInfoVOPage(Page<Album> albumPage, boolean curr) {
        Page<AlbumInfoVO> albumInfoVOPage
                = new Page<>(albumPage.getCurrent(), albumPage.getSize(), albumPage.getTotal(), albumPage.searchCount());
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();
        List<AlbumInfoVO> albumInfoVOList = albumPage.getRecords().stream()
                .map(album -> {
                    AlbumInfoVO albumInfoVO = AlbumInfoVO.albumToVO(album);
                    if (!curr && UserRoleType.FAN.getType().equals(currUser.getType())) {
                        albumInfoVO.setCanLike(Boolean.TRUE);
                        albumInfoVO.setIsLiked(albumLikeMapper.queryByAlbumIdAndUserId(album.getAlbumId(), userId) != null);
                    }
                    return albumInfoVO;
                })
                .collect(Collectors.toList());

        albumInfoVOPage.setRecords(albumInfoVOList);
        return albumInfoVOPage;
    }


}
