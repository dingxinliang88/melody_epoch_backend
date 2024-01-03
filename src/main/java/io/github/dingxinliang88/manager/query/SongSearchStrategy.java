package io.github.dingxinliang88.manager.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.mapper.SongLikeMapper;
import io.github.dingxinliang88.mapper.SongMapper;
import io.github.dingxinliang88.pojo.dto.QueryReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.Song;
import io.github.dingxinliang88.pojo.vo.SearchVO;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.utils.SysUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 搜索歌曲信息策略
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Component
public class SongSearchStrategy implements SearchStrategy {

    @Resource
    private SongMapper songMapper;

    @Resource
    private BandMapper bandMapper;

    @Resource
    private SongLikeMapper songLikeMapper;

    @Override
    public SearchVO doSearch(QueryReq req) {

        String searchText = req.getSearchText();
        Long current = req.getCurrent();
        Long pageSize = req.getPageSize();
        String sortOrder = req.getSortOrder();

        LambdaQueryWrapper<Song> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(Song::getName, searchText); // like searchText%;
        queryWrapper.orderBy(true, CommonConstant.SORT_ORDER_ASC.equals(sortOrder), Song::getName);
        Page<Song> songPage = songMapper.selectPage(new Page<>(current, pageSize), queryWrapper);

        Page<SongInfoVO> songInfoVOPage = convertSongInfoVOPage(songPage);

        // 构造结果
        SearchVO searchVO = new SearchVO();
        searchVO.setSongInfoVOPage(songInfoVOPage);
        return searchVO;
    }

    // --------------------------
    // private util function
    // --------------------------

    private Page<SongInfoVO> convertSongInfoVOPage(Page<Song> songPage) {
        UserLoginVO currUser = SysUtil.getCurrUser();

        Page<SongInfoVO> songInfoVOPage
                = new Page<>(songPage.getCurrent(), songPage.getSize(), songPage.getTotal(), songPage.searchCount());

        Map<Integer, String> bandNameMap = new HashMap<>(16);
        List<SongInfoVO> songInfoVOList = songPage.getRecords().stream().map(song -> {
            SongInfoVO songInfoVO = SongInfoVO.songToVO(song);
            Integer bandId = song.getBandId();
            if (!bandNameMap.containsKey(bandId)) {
                Band band = bandMapper.queryByBandId(bandId, true);
                bandNameMap.put(bandId, band.getName());
            }
            songInfoVO.setBandName(bandNameMap.get(bandId));

            if (UserRoleType.FAN.getType().equals(currUser.getType())) {
                songInfoVO.setCanLike(Boolean.TRUE);
                // TODO 查询次数太多啦，最好一次查出来
                songInfoVO.setIsLiked(songLikeMapper.queryBySongIdAndUserId(songInfoVO.getSongId(), currUser.getUserId()) != null);
            }
            return songInfoVO;
        }).collect(Collectors.toList());

        songInfoVOPage.setRecords(songInfoVOList);
        return songInfoVOPage;
    }
}
