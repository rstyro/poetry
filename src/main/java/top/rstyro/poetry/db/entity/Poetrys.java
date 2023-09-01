package top.rstyro.poetry.db.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author rstyro
 * @since 2023-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Poetrys implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 字符串ID
     */
    private String sid;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 朝代
     */
    private String dynasty;

    /**
     * 作者
     */
    private String author;

    /**
     * 简介
     */
    private String intro;

    /**
     * 标签
     */
    private String tags;

    /**
     * 翻译
     */
    private String translation;

    /**
     * 朗诵地址
     */
    private String voice;

    /**
     * 扩展字段
     */
    private String extData;


}
