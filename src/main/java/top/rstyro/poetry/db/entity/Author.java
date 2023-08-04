package top.rstyro.poetry.db.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author rstyro
 * @since 2023-07-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识，格式：md5(String.format("rs%styro", author))
     */
    private String id;

    private String name;

    private String dynasty;

    private String remark;


}
