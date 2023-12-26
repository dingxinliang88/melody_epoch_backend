package io.github.dingxinliang88.pojo.dto.concert;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 演唱会加入请求
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class JoinConcertReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 演唱会ID
     */
    @NotNull
    private Long concertId;
}
