package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.dingxinliang88.pojo.dto.concert.AddConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.EditConcertReq;
import io.github.dingxinliang88.pojo.dto.concert.JoinConcertReq;
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
     * 获取当前演唱会详细信息
     *
     * @param concertId 演唱会ID
     * @param request   http request
     * @return concert details info vo
     */
    ConcertDetailsVO listCurrConcertInfoVO(Long concertId, HttpServletRequest request);

    /**
     * 加入演唱会
     *
     * @param req     加入演唱会信息
     * @param request http request
     * @return true - 加入成功
     */
    Boolean joinConcert(JoinConcertReq req, HttpServletRequest request);

    /**
     * 取消加入演唱会
     *
     * @param req     退出演唱会信息
     * @param request http request
     * @return true - 退出成功
     */
    Boolean leaveConcert(JoinConcertReq req, HttpServletRequest request);
}
