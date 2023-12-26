package io.github.dingxinliang88.pojo.dto.concert;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 发布演唱会请求
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
public class ReleaseConcertReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    @NotNull
    private Long concertId;
}
