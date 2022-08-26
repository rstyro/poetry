package top.rstyro.poetry.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Accessors(chain = true)
public class PoetrySiShuVo {
    /**
     * 卷
     */
    private String chapter;

    /**
     * 内容
     */
    private List<String> paragraphs;

}
