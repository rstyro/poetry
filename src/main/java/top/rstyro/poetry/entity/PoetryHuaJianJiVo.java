package top.rstyro.poetry.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 花间集 的实体类
 */
@Data
@Accessors(chain = true)
public class PoetryHuaJianJiVo {

    /**
     * 标题
     */
    private String title;
    /**
     * 合集
     */
    private String rhythmic;
    /**
     * 注释
     */
    private List<String> notes;
    /**
     * 作者
     */
    private String author;
    /**
     * 内容
     */
    private List<String> paragraphs;

}
