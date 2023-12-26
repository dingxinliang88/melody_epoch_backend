package io.github.dingxinliang88.pojo.dto.album;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 发布专辑请求
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
public class ReleaseAlbumReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    @NotNull
    private Integer albumId;
}
