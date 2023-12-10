package io.github.dingxinliang88.pojo.dto.album;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改专辑信息请求
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class EditAlbumReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 专辑ID
     */
    @NotNull
    private Integer albumId;


    /**
     * 专辑简介
     */
    @NotNull
    @Length(max = 220, message = "简介长度不能超过220")
    private String profile;

}
