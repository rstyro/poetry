package top.rstyro.poetry.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 宋词 的实体类
 */
@Data
@Accessors(chain = true)
public class PoetrySongCiVo {
    /**
     * 标题
     */
    private String rhythmic;

    /**
     * 作者
     */
    private String author;
    /**
     * 内容
     */
    private List<String> paragraphs;

}
