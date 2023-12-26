package io.github.dingxinliang88.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.dfa.SensitiveUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 敏感词过滤器
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
public class ContentUtil {

    static {
        String projectPath = System.getProperty("user.dir");
        String sensitiveWordsFilePath = projectPath + File.separator + "src/main/resources/data/sensitive_words.txt";
        List<String> words = FileUtil.readLines(sensitiveWordsFilePath, StandardCharsets.UTF_8);
        SensitiveUtil.init(words);
    }

    /**
     * 过滤敏感词
     *
     * @param originContent 原始内容
     * @return 过滤后的内容
     */
    public static String cleanContent(String originContent) {
        return SensitiveUtil.sensitiveFilter(originContent);
    }

}
