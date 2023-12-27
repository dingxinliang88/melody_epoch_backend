package io.github.dingxinliang88.pojo.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.pojo.vo.album.AlbumInfoVO;
import io.github.dingxinliang88.pojo.vo.band.BandInfoVO;
import io.github.dingxinliang88.pojo.vo.concert.ConcertInfoVO;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 搜索VO
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class SearchVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    Page<ConcertInfoVO> concertInfoVOPage;

    Page<AlbumInfoVO> albumInfoVOPage;

    Page<SongInfoVO> songInfoVOPage;

    Page<BandInfoVO> bandInfoVOPage;
}
