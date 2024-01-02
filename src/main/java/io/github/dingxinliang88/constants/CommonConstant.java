package io.github.dingxinliang88.constants;

/**
 * 通用常量类
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
public interface CommonConstant {

    /**
     * 全局序列化ID
     */
    long SYS_SERIALIZABLE_ID = 3630428011452890955L;

    // region pagination
    /**
     * 排序方式：升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 最大每页数量
     */
    Long MAX_PAGE_SIZE_LIMIT = 50L;

    /**
     * 默认每页数量
     */
    Integer DEFAULT_PAGE_SIZE = 15;

    // endregion

    // region release status

    /**
     * 发布状态
     */
    Integer RELEASE = 1;

    /**
     * 未发布状态
     */
    Integer UN_RELEASE = 0;

    // endregion

    // region biz

    /**
     * 歌曲列表分隔符
     */
    String SONGS_STR_SEPARATOR = ",";


    /**
     * 默认简介信息
     */
    String DEFAULT_PROFILE = "这个作者很懒，什么也没有写……";

    // endregion

    // region auth

    Integer BANNED_IDX = 2;

    Integer BANNED = 1 << BANNED_IDX;
    Integer UNBANNED = 0b011;

    // endregion
}
