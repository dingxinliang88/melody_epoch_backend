package io.github.dingxinliang88.constants;

/**
 * 专辑常量类
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
public interface AlbumConstant {

    /**
     * TopN Album Redis Key
     */
    String TOP_ALBUMS_KEY = "album:top:data";

    /**
     * TopN albums expire time (hours)
     */
    int TOP_ALBUMS_EXPIRE_TIME = 12;

    /**
     * TopN num
     */
    int TopN = 10;


    /**
     * TopN album 缓存 epoch key
     * 专辑打分次数超过 epoch 就刷新缓存
     */
    String TOP_ALBUM_CACHE_EPOCH_KEY = "album:top:epoch";

    int EPOCH = 15;

    int INIT_EPOCH = 0;

}
