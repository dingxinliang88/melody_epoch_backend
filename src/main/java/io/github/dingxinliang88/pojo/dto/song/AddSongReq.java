package io.github.dingxinliang88.pojo.dto.song;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加歌曲请求
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class AddSongReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 歌曲名称
     */
    @NotNull(message = "歌曲名称不能为空")
    @Length(min = 1, max = 30, message = "歌曲名称长度必须在1-30之间")
    private String name;

    /**
     * 歌曲作者
     */
    @NotNull(message = "歌曲作者不能为空")
    @Length(min = 1, max = 30, message = "歌曲作者长度必须在1-30之间")
    private String author;
}
