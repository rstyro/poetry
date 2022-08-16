package top.rstyro.poetry.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 唐宋
 */
@Data
@Accessors(chain = true)
public class PoetryTangSongVo {
    /**
     * 标题
     */
    private String title;
    /**
     * 作者
     */
    private String author;

    /**
     * 内容
     */
    private List<String> paragraphs;
    /**
     * 标签，不一定有
     */
    private List<String> tags;

    /**
     * 卷，不一定有
     */
    private String volume;

}
