package io.github.dingxinliang88.constants;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public interface AlbumConstant {

    /**
     * TopN Album Redis Key
     */
    String TOP_ALBUMS_KEY = "album:top";

    /**
     * TopN albums expire time (hours)
     */
    int TOP_ALBUMS_EXPIRE_TIME = 12;

    /**
     * TopN num
     */
    int TopN = 10;

}
