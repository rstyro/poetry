package top.rstyro.poetry.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 作者实体类
 */
@Data
@Accessors(chain = true)
public class PoetryAuthorVo {
    /**
     * 姓名
     */
    private String name;
    /**
     * 描述
     */
    private String desc;

}
