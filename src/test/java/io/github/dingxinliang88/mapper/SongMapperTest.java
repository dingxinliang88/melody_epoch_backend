package io.github.dingxinliang88.mapper;

import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import io.github.dingxinliang88.pojo.vo.song.SongItemVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SongMapperTest {

    @Resource
    private SongMapper songMapper;

    @Test
    public void listSongItemsByBandId() {
        Integer bandId = 1;
        List<SongItemVO> songItemVOs = songMapper.listSongItemsByBandId(bandId);
        System.out.println(songItemVOs);
    }

    @Test
    public void listSongInfoVO() {
        List<SongInfoVO> songInfoVOS = songMapper.listSongInfoVO();
        System.out.println(songInfoVOS);
    }

    @Test
    public void queryCurrAlbumSongs() {
        Integer bandId = 4;
        List<SongItemVO> songItemVOS = songMapper.queryCurrAlbumSongs(bandId, null);
        System.out.println(songItemVOS);
    }
}