package io.github.dingxinliang88.manager.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.mapper.AlbumLikeMapper;
import io.github.dingxinliang88.mapper.AlbumMapper;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.pojo.dto.QueryReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Album;
import io.github.dingxinliang88.pojo.vo.SearchVO;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.utils.SysUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索专辑信息策略
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Component
public class AlbumSearchStrategy implements SearchStrategy {

    @Resource
    private AlbumMapper albumMapper;

    @Resource
    private BandMapper bandMapper;

    @Resource
    private AlbumLikeMapper albumLikeMapper;

    @Override
    public SearchVO doSearch(QueryReq req) {

        String searchText = req.getSearchText();
        Long current = req.getCurrent();
        Long pageSize = req.getPageSize();
        String sortOrder = req.getSortOrder();

        LambdaQueryWrapper<Album> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(Album::getName, searchText); // like searchText%;
        queryWrapper.orderBy(true, CommonConstant.SORT_ORDER_ASC.equals(sortOrder), Album::getName);
        Page<Album> albumPage = albumMapper.selectPage(new Page<>(current, pageSize), queryWrapper);

        Page<AlbumInfoVO> songInfoVOPage = convertAlbumInfoVOPage(albumPage);

        // 构造结果
        SearchVO searchVO = new SearchVO();
        searchVO.setAlbumInfoVOPage(songInfoVOPage);
        return searchVO;
    }

    // --------------------------
    // private util function
    // --------------------------

    private Page<AlbumInfoVO> convertAlbumInfoVOPage(Page<Album> albumPage) {
        Page<AlbumInfoVO> albumInfoVOPage
                = new Page<>(albumPage.getCurrent(), albumPage.getSize(), albumPage.getTotal(), albumPage.searchCount());
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();
        List<AlbumInfoVO> albumInfoVOList = albumPage.getRecords().stream()
                .map(album -> {
                    AlbumInfoVO albumInfoVO = AlbumInfoVO.albumToVO(album);
                    if (UserRoleType.FAN.getType().equals(currUser.getType())) {
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
