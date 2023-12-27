package io.github.dingxinliang88.pojo.dto;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 分页请求体
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
public class PaginationReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    private Long current = 1L;

    @Min(5)
    @Max(50)
    private Long pageSize = 10L;
    private String sortedField;
    /**
     * 排序方式，默认升序
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
