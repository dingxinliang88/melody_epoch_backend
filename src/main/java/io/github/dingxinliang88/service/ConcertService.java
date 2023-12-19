package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.concert.AddConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.EditConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.ReleaseConcertReq;
import io.github.dingxinliang88.pojo.po.Concert;
import io.github.dingxinliang88.pojo.vo.concert.ConcertDetailsVO;
import io.github.dingxinliang88.pojo.vo.concert.ConcertInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Concert Service
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public interface ConcertService extends IService<Concert> {

    /**
     * 添加演唱会信息
     *
     * @param req     添加演唱会请求
     * @param request http request
     * @return 演唱会ID
     */
    Long addConcert(AddConcertReq req, HttpServletRequest request);

    /**
     * 修改演唱会信息
     *
     * @param req     修改演唱会信息
     * @param request http request
     * @return true - 修改成功
     */
    Boolean editInfo(EditConcertReq req, HttpServletRequest request);

    /**
     * 获取演唱会信息
     *
     * @param request http request
     * @return concert info vo
     */
    List<ConcertInfoVO> listConcertInfoVO(HttpServletRequest request);

    /**
     * 发布演唱会信息
     *
     * @param req     发布演唱会请求
     * @param request http request
     * @return concert info vo
     */
    Boolean releaseConcert(ReleaseConcertReq req, HttpServletRequest request);

    /**
     * 撤销发布演唱会信息
     *
     * @param req     发布演唱会请求
     * @param request http request
     * @return concert info vo
     */
    Boolean unReleaseConcert(ReleaseConcertReq req, HttpServletRequest request);

    /**
     * 获取当前乐队的演唱会信息
     *
     * @param request http request
     * @return concert info vo list
     */
    List<ConcertInfoVO> getCurrConcertInfo(HttpServletRequest request);

    /**
     * 获取当前演唱会信息
     *
     * @param concertId concert id
     * @param request   http request
     * @return concert details
     */
    ConcertDetailsVO getCurrConcertDetails(Long concertId, HttpServletRequest request);
}
