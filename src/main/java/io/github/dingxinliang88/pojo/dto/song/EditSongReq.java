package io.github.dingxinliang88.pojo.dto.song;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改歌曲信息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class EditSongReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 歌曲ID
     */
    @NotNull
    private Integer songId;

    /**
     * 乐队ID
     */
    @NotNull
    private Integer bandId;

    /**
     * 所属专辑ID
     */
    @NotNull
    private Integer albumId;

    /**
     * 所属专辑名称
     */
    @NotNull(message = "专辑名称不能为空")
    @Length(min = 1, max = 30, message = "专辑名称长度必须在1-30之间")
    private String albumName;
}
