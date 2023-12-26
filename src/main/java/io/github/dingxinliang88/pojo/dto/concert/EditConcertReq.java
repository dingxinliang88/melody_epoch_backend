package io.github.dingxinliang88.pojo.dto.concert;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 修改演唱会请求
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
public class EditConcertReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 演唱会ID
     */
    @NotNull
    private Long concertId;

    /**
     * 演唱会名称
     */
    @NotNull(message = "演唱会名称不能为空")
    @Length(min = 1, max = 30, message = "演唱会名称长度为1-30")
    private String name;

    /**
     * 开始时间（早于当前时间）
     */
    @NotNull
    private LocalDateTime startTime;

    /**
     * 结束时间（晚于开始时间）
     */
    @NotNull
    private LocalDateTime endTime;

    /**
     * 演唱会地点
     */
    @NotNull(message = "演唱会地点不能为空")
    @Length(min = 1, max = 30, message = "演唱会地点长度为1-30")
    private String place;

    /**
     * 歌曲ID List
     */
    @NotNull(message = "演唱会歌曲不能为空")
    private List<Integer> songIdList;

    /**
     * 演唱会上限人数
     */
    @NotNull(message = "演唱会上限人数不能为空")
    @Max(value = 100_000, message = "演唱会上限人数不能超过100000")
    @Min(value = 0, message = "演唱会上限人数不能小于0")
    private Integer maxNum;
}
