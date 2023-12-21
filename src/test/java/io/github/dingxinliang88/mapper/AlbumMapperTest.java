package io.github.dingxinliang88.mapper;

import io.github.dingxinliang88.constants.CommonConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AlbumMapperTest {

    @Resource
    private AlbumMapper albumMapper;

    @Test
    public void updateAlbumReleaseStatusByAlbumId() {
        Integer albumId = 1;
        albumMapper.updateAlbumReleaseStatusByAlbumId(albumId, CommonConstant.RELEASE);
    }
}