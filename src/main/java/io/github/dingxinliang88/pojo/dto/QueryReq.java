package io.github.dingxinliang88.pojo.dto;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class QueryReq extends PaginationReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 搜索关键字
     */
    @NotNull(message = "搜索关键词不能为空！")
    private String searchText;

    /**
     * 搜索type
     *
     * @see io.github.dingxinliang88.pojo.enums.SearchType
     */
    @NotNull
    @Min(0)
    @Max(4)
    private Integer searchType;
}
