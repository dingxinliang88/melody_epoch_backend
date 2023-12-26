package io.github.dingxinliang88.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dingxinliang88.pojo.vo.song.SongInfoVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SongServiceTest {

    @Resource
    private SongService songService;

    @Test
    public void listSongInfoVOByPage() {
        Page<SongInfoVO> songInfoVOPage = songService.listSongInfoVOByPage(2);
        System.out.println(songInfoVOPage);
    }
}