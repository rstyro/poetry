package top.rstyro.poetry.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 楚辞 的实体类
 */
@Data
@Accessors(chain = true)
public class PoetryChuCiVo {
    /**
     * 标题
     */
    private String title;
    /**
     * 章节
     */
    private String chapter;
    /**
     * 《楚辞》的篇名
     */
    private String section;
    /**
     * 作者
     */
    private String author;
    /**
     * 内容
     */
    private List<String> content;

}
