package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.band.AddBandReq;
import io.github.dingxinliang88.pojo.dto.band.EditBandReq;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.vo.band.BandBriefInfoVO;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Band Service
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public interface BandService extends IService<Band> {

    /**
     * 创建乐队
     *
     * @param req     创建乐队请求
     * @param request http request
     * @return band id
     */
    Integer addBand(AddBandReq req, HttpServletRequest request);


    /**
     * 修改乐队信息（仅队长）
     *
     * @param req     修改乐队信息请求
     * @param request http request
     * @return true - 修改成功
     */
    Boolean editInfo(EditBandReq req, HttpServletRequest request);

    /**
     * 获取乐队简略信息
     *
     * @param request http request
     * @return band brief info
     */
    List<BandBriefInfoVO> listBandBriefInfo(HttpServletRequest request);

    /**
     * 获取乐队详细信息
     *
     * @param bandId  乐队ID
     * @param request http request
     * @return band info vo
     */
    BandInfoVO listBandInfoVO(Integer bandId, HttpServletRequest request);

    /**
     * 发布乐队信息
     *
     * @param request http request
     * @return true - 发布成功
     */
    Boolean releaseBand(HttpServletRequest request);

    /**
     * 撤销发布乐队信息
     *
     * @param request http request
     * @return true - 撤销发布成功
     */
    Boolean unReleaseBand(HttpServletRequest request);


    /**
     * 获取当前登录用户（队长）的乐队详细信息
     *
     * @param request http request
     * @return band info vo
     */
    BandInfoVO listCurrBandInfoVO(HttpServletRequest request);

    /**
     * 查询当前乐队是否已经发布
     *
     * @param request http request
     * @return 1 - 已发布
     */
    Integer queryCurrBandReleaseStatus(HttpServletRequest request);
}
