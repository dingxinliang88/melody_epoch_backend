package io.github.dingxinliang88.manager.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.mapper.BandLikeMapper;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.mapper.MemberMapper;
import io.github.dingxinliang88.pojo.dto.QueryReq;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.BandLike;
import io.github.dingxinliang88.pojo.vo.SearchVO;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;
import io.github.dingxinliang88.utils.SysUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索乐队信息策略
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Component
public class BandSearchStrategy implements SearchStrategy {

    @Resource
    private BandMapper bandMapper;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private BandLikeMapper bandLikeMapper;

    @Override
    public SearchVO doSearch(QueryReq req) {

        String searchText = req.getSearchText();
        Long current = req.getCurrent();
        Long pageSize = req.getPageSize();
        String sortOrder = req.getSortOrder();

        LambdaQueryWrapper<Band> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(Band::getName, searchText); // like searchText%;
        queryWrapper.orderBy(true, CommonConstant.SORT_ORDER_ASC.equals(sortOrder), Band::getName);
        Page<Band> bandPage = bandMapper.selectPage(new Page<>(current, pageSize), queryWrapper);

        Page<BandInfoVO> bandInfoVOPage = convertBandInfoVOPage(bandPage);

        // 构造结果
        SearchVO searchVO = new SearchVO();
        searchVO.setBandInfoVOPage(bandInfoVOPage);
        return searchVO;
    }

    // --------------------------
    // private util function
    // --------------------------

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
}
