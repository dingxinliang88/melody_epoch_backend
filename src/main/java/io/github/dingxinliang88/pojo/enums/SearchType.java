package io.github.dingxinliang88.pojo.enums;

import lombok.Getter;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
public enum SearchType {

    BAND(0, "乐队搜索"),
    ALBUM(1, "专辑搜索"),
    SONG(2, "歌曲搜索"),
    CONCERT(3, "演唱会搜索"),
    ;

    private final Integer type;
    private final String text;

    SearchType(Integer type, String text) {
        this.type = type;
        this.text = text;
    }

    public static SearchType getEnumByType(Integer type) {
        for (SearchType searchEnum : SearchType.values()) {
            if (searchEnum.getType().equals(type)) {
                return searchEnum;
            }
        }
        throw new UnsupportedOperationException("Invalid search strategy type!");
    }
}
