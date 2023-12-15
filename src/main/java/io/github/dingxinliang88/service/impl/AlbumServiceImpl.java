package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.mapper.*;
import io.github.dingxinliang88.pojo.dto.album.AddAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.EditAlbumReq;
import io.github.dingxinliang88.pojo.dto.album.SongToAlbumReq;
import io.github.dingxinliang88.pojo.po.Album;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.Comment;
import io.github.dingxinliang88.pojo.vo.album.AlbumDetailsVO;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import io.github.dingxinliang88.pojo.vo.comment.CommentVO;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.service.AlbumService;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Album Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album>
        implements AlbumService {

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

    @Override
    public AlbumDetailsVO getAlbumDetailsInfo(Integer albumId, HttpServletRequest request) {
        // 获取专辑详细信息
        Album album = albumMapper.queryAlbumByAlbumId(albumId, false);
        ThrowUtil.throwIf(album == null, StatusCode.NOT_FOUND_ERROR, "专辑不存在！");
        AlbumDetailsVO albumDetailsVO = new AlbumDetailsVO(album);
        // 查询专辑的歌曲信息
        List<SongInfoVO> songInfoVOList = songMapper.querySongInfoVOByAlbumId(albumId);
        albumDetailsVO.setSongInfoList(songInfoVOList);
        // 获取专辑的评论信息
        List<Comment> comments = commentMapper.queryByAlbumId(albumId);
        List<CommentVO> commentVOList = parseComments(comments);

        albumDetailsVO.setCommentVOList(commentVOList);

        return albumDetailsVO;
    }

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
        commentVO.setUserName(userName); // Set the user name
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
}
